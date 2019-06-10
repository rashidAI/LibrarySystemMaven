package com.mobilelive.messages;

import java.io.Serializable;

public interface StudentMessages {

    class ActionPerformed implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    class GetStudentsMessage implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
