package com.brohoof.brohoofeconomy;

import java.util.UUID;

public class Account implements Comparable<Account> {

    private int id;
    private UUID uuid;
    private String name;
    private double cash;

    public Account(int id, UUID uuid, String name, double cash) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.cash = cash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Account [id=%s, uuid=%s, name=%s, cash=%s]", id, uuid, name, cash);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account)
            if (uuid.equals(((Account) obj).uuid))
                return true;
        return false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param uuid
     *            the uuid to set
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param cash
     *            the cash to set
     */
    public void setCash(double cash) {
        this.cash = cash;
    }

    @Override
    public int compareTo(Account o) {
        double c1 = cash;
        double c2 = o.cash;
        if(c1 > c2)
            return -1;
        if(c1 < c2)
            return 1;
        return 0;
    }
}
