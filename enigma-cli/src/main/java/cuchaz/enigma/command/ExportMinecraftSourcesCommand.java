package cuchaz.enigma.command;

import cuchaz.enigma.Enigma;
import cuchaz.enigma.EnigmaProject;
import cuchaz.enigma.ProgressListener;
import cuchaz.enigma.source.Decompilers;
import cuchaz.enigma.translation.mapping.EntryMapping;
import cuchaz.enigma.translation.mapping.serde.MappingFormat;
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters;
import cuchaz.enigma.translation.mapping.tree.EntryTree;
import java.io.IOException;
import java.nio.file.Path;

public class ExportMinecraftSourcesCommand extends Command {

    protected ExportMinecraftSourcesCommand() {
        super("export-minecraft-sources");
    }

    @Override public String getUsage() {
        return "<client> <mappings> <out>";
    }

    @Override public boolean isValidArgument(int length) {
        return length == 3;
    }

    @Override public void run(String... args) throws Exception {
        Path clientPath = getReadablePath(getArg(args, 0, "client", true));
        Path mappingsPath = getReadablePath(getArg(args, 1, "mappings", true));
        Path outPath = getReadablePath(getArg(args, 2, "out", true));
        if(outPath == null) return;

        ProgressListener progress = new ConsoleProgressListener();

        Enigma enigma = Enigma.create();
        EnigmaProject project = openProject(clientPath, null);
        MappingSaveParameters saveParameters = enigma.getProfile().getMappingSaveParameters();

        EntryTree<EntryMapping> mappings = MappingFormat.PROGUARD.read(mappingsPath, progress, saveParameters);

        project.setMappings(mappings);

        EnigmaProject.JarExport jar = project.exportRemappedJar(progress);

        jar.decompileStream(progress, Decompilers.PROCYON, EnigmaProject.DecompileErrorStrategy.TRACE_AS_SOURCE)
            .forEach(source -> {
                try {
                    source.writeTo(source.resolvePath(outPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
}
