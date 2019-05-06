package com.example.bespinaf.a2d2.models;

public enum RequestStatus {
    Available, InProgress, Completed, Cancelled ;

    public static RequestStatus getStatusFromString(String statusString) {
        switch (statusString){
            case "Available":
                return RequestStatus.Available;
            case "In Progress":
                return RequestStatus.InProgress;
            case "Completed":
                return RequestStatus.Completed;
            case "Cancelled":
                return RequestStatus.Cancelled;
            default:
                return null;
        }
    }

    public static String getStringFromStatus(RequestStatus status){
        switch (status){
            case Available:
                return "Available";
            case InProgress:
                return "In Progress";
            case Completed:
                return "Completed";
            case Cancelled:
                return "Cancelled";
            default:
                return "N/A";
        }
    }


    @Override
    public String toString() {
        return getStringFromStatus(this);
    }
}

