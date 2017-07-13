package com.app.core;

public class SyncerMan {

    public static String getLatestResponse(String build) {

        String resp = "";

        for (int i = 0; i < Globals.app.qBuilder.size(); i++) {

            if (Globals.app.qBuilder.get(i).build.trim().equalsIgnoreCase(build.trim())) {
                resp = Globals.app.qBuilder.get(i).json;
                break;
            }
        }

        return resp;
    }

    public static void putResponseForSync(String build, String latestJson) {

        int position = -1;
        for (int i = 0; i < Globals.app.qBuilder.size(); i++) {

            if (Globals.app.qBuilder.get(i).build.trim().equalsIgnoreCase(build.trim())) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            Globals.app.qBuilder.get(position).json = latestJson;
        } else {
            Globals.app.qBuilder.add(new QBuilder(build, latestJson));
        }

        // save each time
        Storage.save();
    }

    public static class QBuilder {

        public QBuilder(String build, String json) {
            this.build = build;
            this.json = json;
        }

        String build = "";
        String json = "";
    }

}
