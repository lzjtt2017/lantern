package org.lantern.state;

import java.util.Collection;
import java.util.Collections;

import org.codehaus.jackson.map.annotate.JsonView;
import org.lantern.Events;
import org.lantern.GoogleTalkState;
import org.lantern.LanternHub;
import org.lantern.PeerProxyManager;
import org.lantern.event.GoogleTalkStateEvent;
import org.lantern.event.SyncEvent;
import org.lantern.state.Model.Persistent;
import org.lantern.state.Model.Run;

import com.google.common.eventbus.Subscribe;

/**
 * Class representing data about Lantern's connectivity.
 */
public class Connectivity {
    
    private GoogleTalkState googleTalkState = GoogleTalkState.notConnected;
    
    private String ip = "79.168.34.28";
    
    public Connectivity() {
        Events.register(this);
    }
    
    @JsonView({Run.class})
    public GoogleTalkState getGTalk() {
        return googleTalkState;
    }

    @JsonView({Run.class})
    public boolean getGtalkAuthorized() {
        return false;
    }
    
    @JsonView({Run.class})
    public Collection<Peer> getPeers() {
        return peers(LanternHub.trustedPeerProxyManager());
    }
    
    public Collection<Peer> getPeersCurrent() {
        //return peers(LanternHub.trustedPeerProxyManager());
        return Collections.emptyList();
    }
    
    @JsonView({Run.class, Persistent.class})
    public Collection<Peer> getPeersLifetime() {
        //return peers(LanternHub.trustedPeerProxyManager());
        return Collections.emptyList();
    }
    
    public Collection<Peer> getAnonymousPeers() {
        return peers(LanternHub.anonymousPeerProxyManager());
    }
    
    private Collection<Peer> peers(final PeerProxyManager ppm) {
        return ppm.getPeers().values();
    }

    @Subscribe
    public void onAuthenticationStateChanged(final GoogleTalkStateEvent ase) {
        this.googleTalkState = ase.getState();
        Events.asyncEventBus().post(new SyncEvent(SyncChannel.connectivity));
    }
    
    /*
    @Subscribe
    public void onConnectivityStateChanged(
        final ConnectivityStatusChangeEvent csce) {
        LanternHub.asyncEventBus().post(new SyncEvent(SyncChannel.connectivity));
    }
    */

    @JsonView({Run.class})
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
