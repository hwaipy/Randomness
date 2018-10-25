import java.awt.{Color, Font, RenderingHints}
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.Executors
import javax.imageio.ImageIO

import org.jscience.mathematics.number.Complex

import scala.util.Random

object MultiGaussian extends App {
  //  val executor = Executors.newFixedThreadPool(4)
  //  for (count <- 2 until 3; distance <- 1 until 11) {
  //    executor.submit(new Runnable {
  //      override def run(): Unit = {
  //        calculate(count, distance)
  //      }
  //    })
  //  }
  //  executor.shutdown

  calculateWithPhase(5, 5)

  private def calculate(count: Int, distance: Int) = {
    val beams = (0 until count).map(x => {
      (0 until count).map(y => {
        new GaussianBeam(1e-3, 1, 0.81e-6, x = 1e-3 * x * distance, y = 1e-3 * y * distance)
      })
    }).flatten.toList

    val projector = new BeamProjector(beams)
    val ID = projector.projectI(1e5, (-100, 100), (-100, 100), 1000)
    val center = ID.center
    val diameter = ID.radius * 2
    val mainEnerge = ID.mainBeamEnerge
    ID.plot(new File(s"beam $count*$count-$distance.png"),
      f"""Collimator Matrix $count * $count
         |Single Beam ω0 = 1 mm
         |Beam Distance: $distance mm
         |
          |The screen is placed at z = 100 km with size of 200 m
         |Beam center: x = ${center._1 * 1e3}%1.3f mm, y = ${center._2 * 1e3}%1.3f mm
         |Main Beam (1/e2):
         |    Diameter: $diameter%1.3f m
         |    Divergence: ${diameter / 1e5 * 1e6}%1.3f µrad
         |    Energe: ${mainEnerge * 100}%1.3f%%
         | """.stripMargin)
  }

  private def calculateWithPhase(count: Int, distance: Int) = {
    val beams = (0 until count).map(x => {
      (0 until count).map(y => {
        new GaussianBeam(1e-3, 1, 0.81e-6, x = 1e-3 * x * distance, y = 1e-3 * y * distance, phi0 = Random.nextDouble() * 2 * math.Pi)
      })
    }).flatten.toList

    val projector = new BeamProjector(beams)
    val ID = projector.projectI(1e5, (-100, 100), (-100, 100), 1000)
    val center = ID.center
    val diameter = ID.radius * 2
    val mainEnerge = ID.mainBeamEnerge
    ID.plot(new File(s"beam $count*$count-$distance.png"),
      f"""Collimator Matrix $count * $count
         |Single Beam ω0 = 1 mm
         |Beam Distance: $distance mm
         |
              |The screen is placed at z = 100 km with size of 200 m
         |Beam center: x = ${center._1 * 1e3}%1.3f mm, y = ${center._2 * 1e3}%1.3f mm
         |Main Beam (1/e2):
         |    Diameter: $diameter%1.3f m
         |    Divergence: ${diameter / 1e5 * 1e6}%1.3f µrad
         |    Energe: ${mainEnerge * 100}%1.3f%%
         | """.stripMargin)
  }
}

class GaussianBeam(ω0: Double, E0: Double, λ: Double, x: Double = 0, y: Double = 0, phi0: Double = 0) {
  private def E(r: Double, z: Double): Complex = {
    val amp = E0 * ω0 / ωz(z) * math.exp(-math.pow(r / ωz(z), 2))
    val phase = -k * z - k * math.pow(r, 2) / 2 / R(z) + phi0
    Complex.valueOf(amp * math.cos(phase), amp * math.sin(phase))
  }

  def E(x: Double, y: Double, z: Double): Complex = {
    val r = math.sqrt(math.pow(x - this.x, 2) + math.pow(y - this.y, 2))
    E(r, z)
  }

  private def zr = math.Pi * math.pow(ω0, 2) / λ

  private def ωz(z: Double) = ω0 * math.sqrt(1 + math.pow(z / zr, 2))

  private def k = 2 * math.Pi / λ

  private def R(z: Double) = z * (1 + math.pow(zr / z, 2))
}

class BeamProjector(beams: List[GaussianBeam]) {
  def projectE(z: Double, xRange: Tuple2[Double, Double], yRange: Tuple2[Double, Double], resolution: Int, subResolution: Int = 5) = {
    (0 until resolution).map(x => {
      val xUnitSize = (xRange._2 - xRange._1) / resolution
      val xSubRange = (xRange._1 + xUnitSize * x, xRange._1 + xUnitSize * (x + 1))
      (0 until resolution).map(y => {
        val yUnitSize = (yRange._2 - yRange._1) / resolution
        val ySubRange = (yRange._1 + yUnitSize * y, yRange._1 + yUnitSize * (y + 1))
        calculateSpot(z, xSubRange, ySubRange, subResolution)
      }).toList
    }).toList
  }

  def projectI(z: Double, xRange: Tuple2[Double, Double], yRange: Tuple2[Double, Double], resolution: Int, subResolution: Int = 5) = {
    new IntensityDistribution(projectE(z, xRange, yRange, resolution, subResolution).map(l => l.map(p => math.pow(p.magnitude, 2))), xRange, yRange)
  }

  private def calculateSpot(z: Double, xRange: Tuple2[Double, Double], yRange: Tuple2[Double, Double], resolution: Int) = {
    beams.map(beam => {
      beam.E(xRange._1, yRange._1, z)
    }).reduce { (a, b) => a.plus(b) }
  }
}

class IntensityDistribution(I: List[List[Double]], xRange: Tuple2[Double, Double], yRange: Tuple2[Double, Double]) {
  private def centerPixal = {
    //    val totalI = I.map(line => line.sum).sum
    //    val xCenter = I.map(line => line.zipWithIndex.map(z => z._1 * z._2).sum).sum / totalI
    //    val yCenter = I.zipWithIndex.map(lineZ => {
    //      lineZ._1.map(z => z * lineZ._2).sum
    //    }).sum / totalI
    //    (xCenter, yCenter)

    var xCenter = 0
    var yCenter = 0
    var currentMax = -1.0
    I.zipWithIndex.foreach(line => line._1.zipWithIndex.foreach(z => {
      if (z._1 > currentMax) {
        currentMax = z._1
        yCenter = z._2
        xCenter = line._2
      }
    }))
    (xCenter, yCenter)
  }

  def center = {
    val cP = centerPixal
    (cP._1.toDouble / I.size * (xRange._2 - xRange._1) + xRange._1, cP._2.toDouble / I.size * (yRange._2 - yRange._1) + yRange._1)
  }

  private def radiusPixal = {
    val cP = centerPixal
    val centerI = I(cP._1.toInt)(cP._2.toInt)
    val xStart = (0 until cP._1.toInt).map(x => I(x)(cP._2.toInt) > centerI / math.pow(math.E, 2)).lastIndexOf(false) + 1
    cP._1.toInt - xStart
  }

  def radius = {
    val rP = radiusPixal
    rP.toDouble / I.size * (xRange._2 - xRange._1)
  }

  def mainBeamEnerge = {
    val totalI = I.map(line => line.sum).sum
    val cP = centerPixal
    val r = radiusPixal
    val mainI = (0 until I.size).map(x => {
      (0 until I.size).map(y => {
        math.sqrt(math.pow(x - cP._1, 2) + math.pow(y - cP._2, 2)) <= r match {
          case true => I(x)(y)
          case false => 0
        }
      }).sum
    }).sum
    mainI / totalI
  }

  def plot(file: File, text: String = "") {
    val maxI = I.map(_.max).max
    val minI = 0
    val image = new BufferedImage(I.size, I(0).size, BufferedImage.TYPE_INT_ARGB)
    for (x <- 0 until I.size; y <- 0 until I(0).size) {
      val gray = ((I(x)(y) - minI) / (maxI - minI) * 256).toInt match {
        case g if g < 0 => 0
        case g if g > 255 => 255
        case g => g
      }
      image.setRGB(x, y, new Color(gray, gray, gray).getRGB)
    }

    val c = centerPixal
    image.setRGB(c._1.toInt, c._2.toInt, Color.RED.getRGB)
    val r = radiusPixal

    val g2 = image.createGraphics
    g2.setColor(Color.WHITE)
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    g2.setFont(new Font("Ariel", Font.PLAIN, 20))
    text.split("\n").toList.zipWithIndex.foreach(z => g2.drawString(z._1, 20, 25 + z._2 * 25))
    g2.setColor(Color.RED)
    g2.drawLine(c._1.toInt - r, c._2.toInt, c._1.toInt + r, c._2.toInt)
    g2.drawLine(c._1.toInt, c._2.toInt - r, c._1.toInt, c._2.toInt + r)
    g2.drawLine(c._1.toInt - r, c._2.toInt - 6, c._1.toInt - r, c._2.toInt + 6)
    g2.drawLine(c._1.toInt + r, c._2.toInt - 6, c._1.toInt + r, c._2.toInt + 6)
    g2.drawLine(c._1.toInt - 6, c._2.toInt - r, c._1.toInt + 6, c._2.toInt - r)
    g2.drawLine(c._1.toInt - 6, c._2.toInt + r, c._1.toInt + 6, c._2.toInt + r)

    ImageIO.write(image, "png", file)
  }
}
