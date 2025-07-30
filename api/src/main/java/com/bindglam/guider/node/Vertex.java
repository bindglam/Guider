package com.bindglam.guider.node;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private final int index;
    private final String id;
    private final Location location;
    private final Vertex.Type type;

    private final List<Edge> edges = new ArrayList<>();

    public Vertex(int index, String id, Location location, Type type) {
        this.index = index;
        this.id = id;
        this.location = location;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Type getType() {
        return type;
    }

    public List<Edge> getEdges() {
        return List.copyOf(edges);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public enum Type {
        DEFAULT,
        PORTAL
    }
}
