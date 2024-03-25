
package com.hasan.yakup.bookshelforganizer;

import java.io.Serializable;

/**
 * Represents the loaned history of a book in the bookshelf organizer system.
 * Keeps track of the details regarding a loaned book, including book ID, name,
 * owner ID and name,
 * debtor user ID and name, giving back status, and approval status.
 */
public class LoanedHistory implements Serializable {
    /**
     * The ID of the loaned book.
     */
    private Integer bookId;

    /**
     * The name of the loaned book.
     */
    private String bookName;

    /**
     * The ID of the owner of the loaned book.
     */
    private Integer bookOwnerId;

    /**
     * The name of the owner of the loaned book.
     */
    private String bookOwner;

    /**
     * The ID of the debtor user.
     */
    private Integer debtorUserId;

    /**
     * The name of the debtor user.
     */
    private String debtorUser;

    /**
     * Indicates whether the book has been given back.
     */
    private Boolean hasGivenBack;

    /**
     * Indicates whether the loan is approved.
     */
    private Boolean isApproved;

    /**
     * Constructs a new LoanedHistory object.
     */
    public LoanedHistory() {
    }

    /**
     * Constructs a new LoanedHistory object with specified parameters.
     *
     * @param bookId       The ID of the loaned book.
     * @param bookName     The name of the loaned book.
     * @param bookOwnerId  The ID of the owner of the loaned book.
     * @param bookOwner    The name of the owner of the loaned book.
     * @param debtorUserId The ID of the debtor user.
     * @param debtorUser   The name of the debtor user.
     * @param hasGivenBack Indicates whether the book has been given back.
     * @param isApproved   Indicates whether the loan is approved.
     */
    public LoanedHistory(Integer bookId, String bookName, Integer bookOwnerId, String bookOwner, Integer debtorUserId,
            String debtorUser, Boolean hasGivenBack, Boolean isApproved) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookOwnerId = bookOwnerId;
        this.bookOwner = bookOwner;
        this.debtorUserId = debtorUserId;
        this.debtorUser = debtorUser;
        this.hasGivenBack = hasGivenBack;
        this.isApproved = isApproved;
    }

    /**
     * Gets the name of the loaned book.
     *
     * @return The name of the loaned book.
     */
    public String getBookName() {
        return this.bookName;
    }

    /**
     * Sets the name of the loaned book.
     *
     * @param bookName The name of the loaned book.
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * Gets the name of the owner of the loaned book.
     *
     * @return The name of the owner of the loaned book.
     */
    public String getBookOwner() {
        return this.bookOwner;
    }

    /**
     * Sets the name of the owner of the loaned book.
     *
     * @param ownerName    The first name of the owner of the loaned book.
     * @param ownerSurname The last name of the owner of the loaned book.
     */
    public void setBookOwner(String ownerName, String ownerSurname) {
        this.bookOwner = (ownerName + " " + ownerSurname);
    }

    /**
     * Gets the name of the debtor user.
     *
     * @return The name of the debtor user.
     */
    public String getDebtorUser() {
        return this.debtorUser;
    }

    /**
     * Sets the name of the debtor user.
     *
     * @param userName    The first name of the debtor user.
     * @param userSurname The last name of the debtor user.
     */
    public void setDebtorUser(String userName, String userSurname) {
        this.debtorUser = (userName + " " + userSurname);
    }

    /**
     * Gets the ID of the loaned book.
     *
     * @return The ID of the loaned book.
     */
    public Integer getBookId() {
        return this.bookId;
    }

    /**
     * Sets the ID of the loaned book.
     *
     * @param bookId The ID of the loaned book.
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the ID of the owner of the loaned book.
     *
     * @return The ID of the owner of the loaned book.
     */
    public Integer getBookOwnerId() {
        return this.bookOwnerId;
    }

    /**
     * Sets the ID of the owner of the loaned book.
     *
     * @param bookOwnerId The ID of the owner of the loaned book.
     */
    public void setBookOwnerId(Integer bookOwnerId) {
        this.bookOwnerId = bookOwnerId;
    }

    /**
     * Gets the ID of the debtor user.
     *
     * @return The ID of the debtor user.
     */
    public Integer getDebtorUserId() {
        return this.debtorUserId;
    }

    /**
     * Sets the ID of the debtor user.
     *
     * @param debtorUserId The ID of the debtor user.
     */
    public void setDebtorUserId(Integer debtorUserId) {
        this.debtorUserId = debtorUserId;
    }

    /**
     * Checks if the book has been given back.
     *
     * @return True if the book has been given back, false otherwise.
     */
    public Boolean getHasGivenBack() {
        return this.hasGivenBack;
    }

    /**
     * Sets the giving back status of the book.
     *
     * @param hasGiven Indicates whether the book has been given back.
     */
    public void setHasGivenBack(Boolean hasGiven) {
        this.hasGivenBack = hasGiven;
    }

    /**
     * Checks if the loan is approved.
     *
     * @return True if the loan is approved, false otherwise.
     */
    public Boolean getIsApproved() {
        return this.isApproved;
    }

    /**
     * Sets the approval status of the loan.
     *
     * @param isApproved Indicates whether the loan is approved.
     */
    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
