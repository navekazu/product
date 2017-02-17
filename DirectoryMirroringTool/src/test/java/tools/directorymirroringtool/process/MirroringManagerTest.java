package tools.directorymirroringtool.process;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MirroringManagerTest {
    @Test
    public void createMirroringProcess() throws Exception {
        MirroringManager mirroringManager = new MirroringManager();
        mirroringManager.createMirroringProcess(Paths.get("D:\\work\\test01\\source"), Paths.get("D:\\work\\test01\\sink"));
    }

}