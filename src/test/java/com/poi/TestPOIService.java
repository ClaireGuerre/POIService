package com.poi;

import com.poi.model.Area;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class TestPOIService {
    private POIService poiService;
    private Path inputFilePath;

    @Before
    public void initialize() {
        poiService = new POIService();

        try {
            inputFilePath = Files.createTempFile("test","tsv");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("@id @lat @lon");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id1 -48.6 -37.7");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id2 -27.1 8.4");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id3 6.6 -6.9");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id4 -2.3 38.3");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id5 6.8 -6.9");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id6 -2.5 38.3");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id7 0.1 -0.1");
            stringBuffer.append(System.lineSeparator());
            stringBuffer.append("id8 -2.1 38.1");
            byte[] content = stringBuffer.toString().getBytes();
            Files.write(inputFilePath, content);
        } catch (IOException e) {
            fail("Unable to create input file");
        }
    }

    @Test
    public void testGetNbPOI() {
        try {
            assertTrue(poiService.getNbPOI(6.5,-7, inputFilePath.toFile()) == 2);
        } catch (IOException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetNHeaviestAreas() {
        List<Area> areas = null;
        try {
            areas = poiService.getNHeaviestAreas(2, inputFilePath.toFile());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertNotNull(areas);
        assertTrue(areas.size() == 2);
        Area firstArea = areas.get(0);
        assertTrue(firstArea.getMinLat() == -2.5);
        assertTrue(firstArea.getMaxLat() == -2);
        assertTrue(firstArea.getMinLon() == 38);
        assertTrue(firstArea.getMaxLon() == 38.5);

        Area secondArea = areas.get(1);
        assertTrue(secondArea.getMinLat() == 6.5);
        assertTrue(secondArea.getMaxLat() == 7);
        assertTrue(secondArea.getMinLon() == -7);
        assertTrue(secondArea.getMaxLon() == -6.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void coordinatesOutOfScopeShouldThrowException() {
        try {
            poiService.getNbPOI(-95, 0.5, inputFilePath.toFile());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullFileInputShouldThrowException() {
        try {
            poiService.getNbPOI(0,0, null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
