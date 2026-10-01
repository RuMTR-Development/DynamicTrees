package com.dtteam.dynamictrees.treepack;

import com.dtteam.dynamictrees.api.resource.TreeResourcePack;import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.minecraft.FileUtil;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;import java.util.stream.Collectors;
import java.util.stream.Stream;

//? if >= 1.21 {
import net.minecraft.server.packs.PackLocationInfo;
//? }

//? if >= 1.19.4 {
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
//? } else {

//? }

/**
 * Credits: A lot of the file reading code was based off PathPackResources.
 *
 * @author Harley O'Connor
 */
//~ if < 1.19.4 'PathPackResources' -> 'AbstractPackResources'
public class TreePackResources extends PathPackResources implements com.dtteam.dynamictrees.api.resource.TreeResourcePack {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path root;

    //? if >= 1.21.1 {
    public TreePackResources(PackLocationInfo location, Path root) {
        super(location, root);
        this.root = root;
    }
    //? } else if >= 1.19.4 {
    /*public TreePackResources(String name, Path root, boolean isBuiltin) {
        super(name, root, isBuiltin);
        this.root = root;
    }
    *///? } else {
    /*public TreePackResources(Path root) {
        super(root.toFile());
        this.root = root;
    }
    *///? }

    //? if >= 1.19.4 {
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        return this.getRootResource(getPathFromLocation(location));
    }
    //? } else {
    /*@Override
    public InputStream getResource(PackType type, ResourceLocation location) throws IOException {
        return this.getRootResource(
                String.join("/", getPathFromLocation(location))
        );
    }
    *///? }

    private static String[] getPathFromLocation(ResourceLocation location) {
        String[] parts = location.getPath().split("/");
        String[] result = new String[parts.length + 1];
        result[0] = location.getNamespace();
        System.arraycopy(parts, 0, result, 1, parts.length);
        return result;
    }

    //? if >= 1.21 {
    @Override
    public void listResources(@Nullable PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        FileUtil.decomposePath(path)
                .ifSuccess(parts -> net.minecraft.server.packs.PathPackResources.listPath(namespace, this.root.resolve(namespace).toAbsolutePath(), parts, resourceOutput))
                .ifError(dataResult -> LOGGER.error("Invalid path {}: {}", path, dataResult.message()));
    }
    //? } else if >= 1.19.4 {
    /*@Override
    public void listResources(@Nullable PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        DataResult<List<String>> result = FileUtil.decomposePath(path);

        result.result().ifPresent(parts -> net.minecraft.server.packs.PathPackResources.listPath(namespace, this.root.resolve(namespace).toAbsolutePath(), parts, resourceOutput));
        result.error().ifPresent(dataResult -> LOGGER.error("Invalid path {}: {}", path, dataResult.message()));
    }
    *///? } else {
    /*@Override
    public Collection<ResourceLocation> getResources(PackType type, String namespace, String path, Predicate<ResourceLocation> filter) {
        List<ResourceLocation> result = new ArrayList<>();
        listPath(namespace, this.root.resolve(namespace).toAbsolutePath(), Arrays.asList(path.split("/")), (location, supplier) -> result.add(location));
        return result;
    }
    *///? }

    @Override
    public Set<String> getNamespaces(@Nullable PackType type) {
        try {
            try (Stream<Path> walker = Files.walk(this.root, 1)) {
                return walker
                        .filter(Files::isDirectory)
                        .map(this.root::relativize)
                        .filter(p -> p.getNameCount() > 0) // Skip the root entry
                        .map(p -> p.toString().replaceAll("/$", "")) // Remove the trailing slash, if present
                        .filter(s -> !s.isEmpty()) // Filter empty strings, otherwise empty strings default to minecraft namespace in ResourceLocations
                        .collect(Collectors.toSet());
            }
        } catch (IOException | AssertionError e) {
            return Set.of();
        }
    }

    //? if < 1.19.4 {
    /*private static final Joiner PATH_JOINER = Joiner.on("/");

    public static Path resolvePath(Path root, List<String> path) {
        int length = path.size();

        switch (length) {
            case 0:
                return root;

            case 1:
                return root.resolve(path.get(0));

            default:
                String[] remainingPath = new String[length - 1];

                for(int j = 1; j < length; ++j) {
                    remainingPath[j - 1] = path.get(j);
                }

                return root.resolve(root.getFileSystem().getPath(path.get(0), remainingPath));
        }
    }

    public InputStream getRootResource(String fullPath) throws IOException {
        Path path = resolvePath(this.root, Arrays.asList(fullPath.split("/")));

        if (!Files.exists(path)) {
            throw new IOException("Resource missing");
        }

        return Files.newInputStream(path);
    }

    public static void listPath(String namespace, Path namespacePath, List<String> decomposedPath, BiConsumer<ResourceLocation, Resource.IoSupplier<InputStream>> resourceOutput) {
        Path resolved = resolvePath(namespacePath, decomposedPath);

        try (Stream<Path> stream = Files.find(resolved, Integer.MAX_VALUE, (file, attrs) -> attrs.isRegularFile())) {
            stream.forEach((path) -> {
                String s = PATH_JOINER.join(namespacePath.relativize(path));
                ResourceLocation resourcelocation = ResourceLocation.tryBuild(namespace, s);
                if (resourcelocation == null) {
                    Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", namespace, s));
                } else {
                    resourceOutput.accept(resourcelocation, () -> Files.newInputStream(path));
                }

            });
        } catch (NoSuchFileException ignored) {
        } catch (IOException e) {
            LOGGER.error("Failed to list path {}", resolved, e);
        }

    }

    @Override
    public void close() {}

    @Override
    protected InputStream getResource(String resourcePath) throws IOException {
        return this.getRootResource(resourcePath);
    }

    @Override
    protected boolean hasResource(String resourcePath) {
        try {
            this.getRootResource(resourcePath);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
    *///? }
}