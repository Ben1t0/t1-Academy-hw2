package ru.t1academy.java.hw2.configuration;

import java.util.*;

public class LoggerSettings {
    Set<IncludeTypes> include = new HashSet<>();

    public Set<IncludeTypes> getInclude() {
        return include;
    }

    public void setInclude(Set<IncludeTypes> include) {
        this.include = include;
    }
}
