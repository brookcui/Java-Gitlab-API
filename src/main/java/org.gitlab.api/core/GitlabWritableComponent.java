package org.gitlab.api.core;

interface GitlabWritableComponent<C extends GitlabComponent> extends GitlabComponent {
    C create();

    C delete();
}
