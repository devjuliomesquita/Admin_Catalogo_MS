package com.juliomesquita.admin.catalog.domain.commom.exceptions;

import com.juliomesquita.admin.catalog.domain.commom.validation.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMassege, final Notification notification) {
        super(aMassege, notification.getErrors());
    }
}
