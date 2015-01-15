package at.fhv.puzzle2.server.client;

public class Team {
    private Client _unityClient;
    private Client _mobileClient;

    public void setUnityClient(Client unityClient) {
        _unityClient = unityClient;
    }

    public Client getUnityClient() {
        return _unityClient;
    }

    public void setMobileClient(Client mobileClient) {
        _mobileClient = mobileClient;
    }

    public Client getMobileClient() {
        return _mobileClient;
    }

    public boolean isTeamReady() {
        return _mobileClient.isReady() && _unityClient.isReady();
    }
}
