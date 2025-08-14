open module PastefyAPI.main {
    requires transitive com.google.gson;
    requires org.jetbrains.annotations;
    requires java.net.http;
    requires org.bouncycastle.provider;

    exports de.greensurvivors;
}