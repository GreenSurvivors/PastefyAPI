open module PastefyAPI.main {
    requires transitive com.google.gson;
    requires org.jetbrains.annotations;
    requires java.net.http;
    requires org.bouncycastle.provider;
    requires PastefyAPI.main;

    exports de.greensurvivors;
    exports de.greensurvivors.exception;
    exports de.greensurvivors.reply;
    exports de.greensurvivors.admin;
    exports de.greensurvivors.queryparam;
}