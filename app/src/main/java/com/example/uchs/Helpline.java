package com.example.uchs;

public class Helpline {
    public String helplineName;
    public String helplinePh;
    public String helplineID;
    public String helplinePass;
    public String helplineType;
    public String helplineLocation;

    Helpline(String name, String phone, String hid, String hPass, String hType, String hLocation) {
        helplineName = name.replace(" ","-");
        helplinePh = phone;
        helplineID = hid;
        helplinePass = hPass;
        helplineType = hType;
        helplineLocation = hLocation;
    }
    public String genHelpString() {
        String msg = "\n";
        msg += "HelpName: " + helplineName + "\n";
        msg += "HelpPhone: " + helplinePh + "\n";
        msg += "HelpID: " + helplineID + "\n";
        msg += "HelpPass: " + helplinePass + "\n";
        msg += "HelpType: " + helplineType + "\n";
        msg += "HelpLocation: " + helplineLocation + "\n";
        return msg;
    }
}
