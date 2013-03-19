package data;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/25/13
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    private int id;
    private String name;
    private String password;
    private int score;
    private boolean isOnline;

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
        score = 0;
        isOnline = true;
        id = -1;
    }

    public Player(){
        id = 0;
        score = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }



    @Override
    public String toString() {
        return "common.entity.AuthorisationInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", score=" + score +
                ", isOnline=" + isOnline +
                '}';
    }
}
