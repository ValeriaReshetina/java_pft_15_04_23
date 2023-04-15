package ru.stqa.pft.soap;

import com.lavasoft.GeoIPService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GeoIpServiceTests {

    @Test
    public void testMyIp() {
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("188.191.245.70");
        Assert.assertTrue(ipLocation.contains("RU"));
    }

    @Test
    public void testInvalidIp() {
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("188.191.245.xx]]");
        Assert.assertTrue(ipLocation.contains("RU"));
    }
}
