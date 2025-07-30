package com.bindglam.guider.util;

import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class MathUtils {
    private MathUtils() {
    }

    public static double distanceSquared(Location loc1, Location loc2) {
        if(loc1.getWorld() != loc2.getWorld())
            return 0.0;

        return NumberConversions.square(loc1.getX() - loc2.getX()) + NumberConversions.square(loc1.getY() - loc2.getY()) + NumberConversions.square(loc1.getZ() - loc2.getZ());
    }

    public static double distance(Location loc1, Location loc2) {
        return Math.sqrt(distanceSquared(loc1, loc2));
    }

    public static float lerp(float a, float b, float alpha) {
        return a * (1 - alpha) + b * alpha;
    }

    public static double lerp(double a, double b, double alpha) {
        return a * (1 - alpha) + b * alpha;
    }

    public static Location lerp(Location loc1, Location loc2, float alpha) {
        double x = lerp(loc1.getX(), loc2.getX(), alpha);
        double y = lerp(loc1.getY(), loc2.getY(), alpha);
        double z = lerp(loc1.getZ(), loc2.getZ(), alpha);

        Quaternionf loc1Rot = new Quaternionf().rotateXYZ((float) Math.toRadians(loc1.getPitch()), (float) Math.toRadians(loc1.getYaw()), 0f);
        Quaternionf loc2Rot = new Quaternionf().rotateXYZ((float) Math.toRadians(loc2.getPitch()), (float) Math.toRadians(loc2.getYaw()), 0f);

        Vector3f rot = loc1Rot.slerp(loc2Rot, alpha).getEulerAnglesXYZ(new Vector3f());

        return new Location(loc2.getWorld(), x, y, z, rot.y, rot.x);
    }
}
