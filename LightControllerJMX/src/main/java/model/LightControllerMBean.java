package model;

public interface LightControllerMBean {

    void setSequence(String sequence);

    String getSequence();

    void switchLight(int id);

    void switchSimulation();

}