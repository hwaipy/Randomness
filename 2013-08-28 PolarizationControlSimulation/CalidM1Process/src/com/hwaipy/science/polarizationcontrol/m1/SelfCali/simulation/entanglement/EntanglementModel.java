package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import java.util.Random;
import simulationconsole.entanglement.FiberTransform;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;
import simulationconsole.entanglement.SinglePartyOperator;
import simulationconsole.entanglement.TwoPhotonState;
import simulationconsole.entanglement.WavePlate;

/**
 *
 * @author Hwaipy
 */
public class EntanglementModel {

    private boolean debug;
    private Random random = null;
    private FiberTransform ft1;
    private FiberTransform ft2;
    private final WavePlate qwp11 = QuarterWavePlate.create(0);
    private final WavePlate qwp12 = QuarterWavePlate.create(0);
    private final WavePlate hwp1 = HalfWavePlate.create(0);
    private final WavePlate qwp21 = QuarterWavePlate.create(0);
    private final WavePlate qwp22 = QuarterWavePlate.create(0);
    private final WavePlate hwp2 = HalfWavePlate.create(0);
    private final ErroredTwoPhotonStateFactory twoPhotonStateFactory;
    private TwoPhotonState twoPhotonState;

    public EntanglementModel(ErroredTwoPhotonStateFactory twoPhotonStateFactory) {
        random = new Random(0);
        ft1 = FiberTransform.createRandomFiber(random);
        ft2 = FiberTransform.createRandomFiber(random);
        this.twoPhotonStateFactory = twoPhotonStateFactory;
        twoPhotonState = twoPhotonStateFactory.generateRandomErroredTwoPhotonState();
    }

    public void set(int group, int number, double angle) {
        WavePlate wavePlate;
        switch (group * 3 + number) {
            case 4:
                wavePlate = qwp11;
                break;
            case 5:
                wavePlate = qwp12;
                break;
            case 6:
                wavePlate = hwp1;
                break;
            case 7:
                wavePlate = qwp21;
                break;
            case 8:
                wavePlate = qwp22;
                break;
            case 9:
                wavePlate = hwp2;
                break;
            default:
                throw new RuntimeException();
        }
        wavePlate.setTheta(angle);
    }

    public double[][] readCoindicences() {
        TwoPhotonState entanglement = twoPhotonState.evolution(ft1, SinglePartyOperator.I)
                .evolution(SinglePartyOperator.I, ft2)
                .evolution(qwp11, SinglePartyOperator.I)
                .evolution(qwp12, SinglePartyOperator.I)
                .evolution(hwp1, SinglePartyOperator.I)
                .evolution(SinglePartyOperator.I, qwp21)
                .evolution(SinglePartyOperator.I, qwp22)
                .evolution(SinglePartyOperator.I, hwp2);
        double[][] coincidents = entanglement.coincidents();
        return coincidents;
    }

    public void generateRandomFiberTransform() {
        ft1 = FiberTransform.createRandomFiber(random);
        ft2 = FiberTransform.createRandomFiber(random);
    }

    public void generateRandomTwoPhotonStateError() {
        twoPhotonState = twoPhotonStateFactory.generateRandomErroredTwoPhotonState();
    }

    public void show() {
        System.out.println("-----------------------------------");
        twoPhotonState.show();
        System.out.println("-----------------------------------");
    }
}
