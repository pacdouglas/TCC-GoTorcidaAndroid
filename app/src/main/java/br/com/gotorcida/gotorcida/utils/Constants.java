package br.com.gotorcida.gotorcida.utils;

public class Constants {
    private static final String URL_SERVER = "http://192.168.0.10:8080/gotorcidaws/";
    public static final String URL_SERVER_JSON_FIND_EVENT = URL_SERVER + "event/find";
    public static final String URL_SERVER_JSON_LIST_EVENTS_BY_TEAM = URL_SERVER + "event/listByTeam";
    public static final String URL_SERVER_JSON_LIST_EVENTS_BY_USER = URL_SERVER + "event/listByUser";
    public static final String URL_SERVER_JSON_LIST_EVENTS_BY_SPORT = URL_SERVER + "event/listBySport";
    public static final String URL_SERVER_JSON_INSERT_EVENT = URL_SERVER + "/event/save";
    public static final String URL_SERVER_JSON_SET_EVENT_RESULT = URL_SERVER + "eventResult/update";
    public static final String URL_SERVER_JSON_LIST_NEWS = URL_SERVER + "news/find";
    public static final String URL_SERVER_JSON_UPDATE_NEWS = URL_SERVER + "news/update";
    public static final String URL_SERVER_JSON_INSERT_NEWS = URL_SERVER + "news/save";
    public static final String URL_SERVER_JSON_FIND_ATHLETE = URL_SERVER + "athlete/findAthleteOnTeam";
    public static final String URL_SERVER_JSON_LIST_SPORTS = URL_SERVER + "sport";
    public static final String URL_SERVER_JSON_FIND_TEAM = URL_SERVER + "team";
    public static final String URL_SERVER_JSON_TEAM_UPDATE = URL_SERVER + "team/update";
    public static final String URL_SERVER_JSON_LIST_TEAMS = URL_SERVER + "team";
    public static final String URL_SERVER_JSON_LIST_AVAILABLE_ATHLETES = URL_SERVER + "athlete/findAvailable";
    public static final String URL_SERVER_JSON_LIST_ATHLETES_FROM_TEAM = URL_SERVER + "athlete";
    public static final String URL_SERVER_JSON_ADD_ATHLETE = URL_SERVER + "athlete/save";
    public static final String URL_SERVER_JSON_UPDATE_ATHLETE = URL_SERVER + "athlete/update";
    public static final String URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT = URL_SERVER + "position/listBySport";
    public static final String URL_SERVER_JSON_SAVE_ATHLETE_ON_TEAM = URL_SERVER + "team/saveAthleteOnTeam";
    public static final String URL_SERVER_DASHBOARD_SAVECONFIG = URL_SERVER + "dashboardConfig";
    public static final String URL_SERVER_NEW_USER = URL_SERVER + "user";
    public static final String URL_SERVER_FIND_USER = URL_SERVER + "user/find";
    public static final String URL_SERVER_LOGIN = URL_SERVER + "login";
    public static final String URL_IMAGES_BASE = "http://gotorcida.com.br/img/";
    public static final int SLEEP_THREAD = 1000;
}
