open module PastefyAPI.main {
    requires transitive com.google.gson;
    requires org.jetbrains.annotations;
    requires java.net.http;
    requires org.bouncycastle.provider;
    // Note for future head scratching: NO 'requires PastefyAPI.main;' does NOT belong here. The IDE just adds it out of spite.

    exports de.greensurvivors;
    exports de.greensurvivors.exception;
    exports de.greensurvivors.reply;
    exports de.greensurvivors.admin;
    exports de.greensurvivors.queryparam;
}