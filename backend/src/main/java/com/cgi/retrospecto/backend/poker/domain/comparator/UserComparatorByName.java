package com.cgi.retrospecto.backend.poker.domain.comparator;

import com.cgi.retrospecto.backend.poker.domain.User;

import java.util.Comparator;

public class UserComparatorByName implements Comparator<User> {
        @Override
        public int compare(User u1, User u2) {
            return u1.getUsername().compareTo(u2.getUsername());
        }
}
