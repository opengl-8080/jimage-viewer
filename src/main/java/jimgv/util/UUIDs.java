package jimgv.util;

import java.util.UUID;

public class UUIDs {
    private static String testUUID;

    public static String newUUID() {
        if (UUIDs.testUUID != null) {
            return UUIDs.testUUID;
        }

        return UUID.randomUUID().toString();
    }

    public static void setTestUUID(String uuid) {
        UUIDs.testUUID = uuid;
    }
}
