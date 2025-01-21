package org.apache.coyote.http11.cookie;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final Map<String, Session> SESSIONS = new HashMap<>();

    public SessionManager() {}

    public void add(Session session) {
        SESSIONS.put(session.getId(), session);
    }

    public Session findSession(final String id) {
        return SESSIONS.get(id);
    }

    public void remove(Session session) {
        SESSIONS.remove(session.getId());
    }
}
