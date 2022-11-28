package com.donation.domain.notification.entity;


import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public enum NotifyType {
    POST{
        @Override
        public String toMessage() {
            return null;
        }
    },
    DONATE{
        @Override
        public String toMessage() {
            return null;
        }
    },
    COMMENT{
        @Override
        public String toMessage() {
            return null;
        }
    },
    REPLY{
        @Override
        public String toMessage() {
            return null;
        }
    },
    LIKE{
        @Override
        public String toMessage() {
            return null;
        }
    };

    public abstract String toMessage();
}
