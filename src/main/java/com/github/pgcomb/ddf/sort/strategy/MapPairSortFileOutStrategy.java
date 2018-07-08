package com.github.pgcomb.ddf.sort.strategy;

import com.github.pgcomb.ddf.common.packagee.FileDatePackage;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.exception.SortOutException;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * mapPair 排序后输出到文件策略 {@link MapPair}
 *
 * @author 王东旭
 */
public class MapPairSortFileOutStrategy implements SortOutStrategy<InMemoryDataPackage<MapPair<Principal, Payload>>, FileDatePackage> {

    private static final Logger log = LoggerFactory.getLogger(MapPairSortFileOutStrategy.class);

    /**
     * 文件输出的base path
     */
    private Path basePath = Paths.get(System.getProperties().getProperty("user.home")).resolve("DDF");

    private static final String EXPANDED_NAME = "mpst";

    public MapPairSortFileOutStrategy(Path basePath) {
        this.basePath = basePath;
        init();
    }

    public MapPairSortFileOutStrategy() {
        init();
    }

    public void init(){
        basePath.toFile().mkdirs();
    }

    @Override
    public FileDatePackage export(InMemoryDataPackage<MapPair<Principal, Payload>> inMemoryDataPackage) throws SortOutException {

        LocalDateTime start = LocalDateTime.now();
        FileDatePackage fileDatePackage = new FileDatePackage(getFile(inMemoryDataPackage), inMemoryDataPackage);
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(getFile(inMemoryDataPackage)), "utf-8"))) {
            for (MapPair mapPair:inMemoryDataPackage.getData()) {
                bufferedWriter.write(mapPair.strValue());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new SortOutException("mapPair export to file error", e);
        }
        log.info("export package[serialNumber:{},start:{},end{},size{}]:{}",inMemoryDataPackage.serialNumber(),
                inMemoryDataPackage.start(),inMemoryDataPackage.end(),inMemoryDataPackage.size(), Duration.between(start,LocalDateTime.now()));
        log.debug("export:{}",fileDatePackage);
        return fileDatePackage;
    }

    public File getFile(InMemoryDataPackage p) {

        return basePath.resolve(String.format("sort-%s(%s-%s).%s", p.serialNumber(), p.start(), p.end(), EXPANDED_NAME)).toFile();

    }
}
