/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.reporting.util;

import com.casabots.pantry.reporting.model.Location;

public class LocationUtil {

    private static final double MIN_LAT = Math.toRadians(-90d);  // -PI/2
    private static final double MAX_LAT = Math.toRadians(90d);   //  PI/2
    private static final double MIN_LON = Math.toRadians(-180d); // -PI
    private static final double MAX_LON = Math.toRadians(180d);  //  PI
    private static final double RADIUS  = 6371.01;

    private LocationUtil() {
    }
    /**
     * <p>Computes the bounding coordinates of all points on the surface
     * of a sphere that have a great circle distance to the point represented
     * by this LocationUtil instance that is less or equal to the distance
     * argument.</p>
     * @param location the location for which bounding coordinates will be calculated
     * @param distance the distance from the point represented by this
     * location instance. Must me measured in the same unit is km.
     * @return an array of two Location objects such that:<ul>
     * <li>The latitude of any point within the specified distance is greater
     * or equal to the latitude of the first array element and smaller or
     * equal to the latitude of the second array element.</li>
     * <li>If the longitude of the first array element is smaller or equal to
     * the longitude of the second element, then
     * the longitude of any point within the specified distance is greater
     * or equal to the longitude of the first array element and smaller or
     * equal to the longitude of the second array element.</li>
     * <li>If the longitude of the first array element is greater than the
     * longitude of the second element (this is the case if the 180th
     * meridian is within the distance), then
     * the longitude of any point within the specified distance is greater
     * or equal to the longitude of the first array element
     * <strong>or</strong> smaller or equal to the longitude of the second
     * array element.</li>
     * </ul>
     */
    public static Location[] boundingCoordinates(Location location, double distance) {

        if (distance < 0d)
            throw new IllegalArgumentException();

        // angular distance in radians on a great circle
        double radDist = distance / RADIUS;

        double minLat = Math.toRadians(location.getLatitude()) - radDist;
        double maxLat = Math.toRadians(location.getLongitude()) + radDist;

        double minLon, maxLon;
        if (minLat > MIN_LAT && maxLat < MAX_LAT) {
            double deltaLon = Math.asin(Math.sin(radDist) /
                    Math.cos(Math.toRadians(location.getLatitude())));
            minLon = Math.toRadians(location.getLongitude()) - deltaLon;
            if (minLon < MIN_LON) minLon += 2d * Math.PI;
            maxLon = Math.toRadians(location.getLongitude()) + deltaLon;
            if (maxLon > MAX_LON) maxLon -= 2d * Math.PI;
        } else {
            // a pole is within the distance
            minLat = Math.max(minLat, MIN_LAT);
            maxLat = Math.min(maxLat, MAX_LAT);
            minLon = MIN_LON;
            maxLon = MAX_LON;
        }

        return new Location[]{new Location(Math.toDegrees(minLat), Math.toDegrees(minLon)),
                new Location(Math.toDegrees(maxLat), Math.toDegrees(maxLon))};
    }

    public static double getDeltaLatitude(double distance) {
        return distance/RADIUS;
    }
}
