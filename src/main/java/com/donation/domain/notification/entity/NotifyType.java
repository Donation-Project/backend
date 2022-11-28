package com.donation.domain.notification.entity;


import lombok.Getter;

import static com.donation.domain.notification.entity.NotificationMessage.*;

@Getter
public enum NotifyType {
    POST{
        @Override
        public String toMessage() {
            return NOTIFY_POST_MESSAGE;
        }
    },
    DONATE{
        @Override
        public String toMessage() {
            return NOTIFY_DONATE_MESSAGE;
        }
    },
    REPLY{
        @Override
        public String toMessage() {
            return NOTIFY_REPLY_COMMENT_MESSAGE;
        }
    },
    LIKE{
        @Override
        public String toMessage() {
            return NOTIFY_LIKE_MESSAGE;
        }
    };

    public abstract String toMessage();
}
