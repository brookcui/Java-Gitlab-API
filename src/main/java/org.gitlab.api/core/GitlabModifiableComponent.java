package org.gitlab.api.core;

 interface GitlabModifiableComponent<C extends GitlabComponent> extends GitlabWritableComponent<C> {
    C update();
}
