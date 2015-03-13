package D20150312.BirthdayParadox;

/**
 *
 * @author Hwaipy
 */
public class BirthdayParadox {

  public static void main(String[] args) {
//    for (int i = 0; i < 1000; i++) {
//      System.out.println(i + "\t" + match((int) (i * i / Math.sqrt(2)), i));
//    }
    System.out.println(match(366, 23));
  }

  private static double match(int days, int persons) {
    double p = 1;
    for (int n = 1; n < persons; n++) {
      p *= (1 - n / (double) days);
    }
    return 1 - p;
  }

}
