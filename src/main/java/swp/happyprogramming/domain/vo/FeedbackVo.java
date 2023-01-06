package swp.happyprogramming.domain.vo;


public class FeedbackVo {
    private static final long serialVersionUID = 1L;
    private String senderName;
    private String receiverName;

    private Integer rate;

    private String comment;

    public FeedbackVo(String senderName, Integer rate, String comment) {
        this.senderName = senderName;
        this.rate = rate;
        this.comment = comment;
    }


    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Integer getRate() {
        return this.rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



}
