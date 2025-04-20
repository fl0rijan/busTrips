import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.model.*;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.abs;

public class busTrips {
    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
            ArrayList<String> routes = new ArrayList<>();
            ArrayList<ArrayList<String>> route_times = new ArrayList<>();

            LocalTime Current_time = LocalTime.now();

            String id_postaje = args[0];
            String st_nasljednjih_postaj = args[1];
            String oblika_casa = args[2];

            GtfsReader reader = new GtfsReader();
            reader.setInputLocation(new File("gtfs"));

            GtfsDaoImpl store = new GtfsDaoImpl();
            reader.setEntityStore(store);

            reader.run();

            Agency[] agency = store.getAllAgencies().toArray(new Agency[0]);
            AgencyAndId id_stop = AgencyAndId.convertFromString(agency[0].getId() + "_" + id_postaje);

            Stop stop = store.getStopForId(id_stop);
            if (stop == null) {
                System.out.println("Stop is null");
                System.exit(-1);
            }

            String stop_name = stop.getName();
            System.out.println("Postajališče " + stop_name);
            StopTime[] stop_times = store.getAllStopTimes().toArray(new StopTime[0]);

            for (StopTime times : stop_times) {
                StopLocation stop_id = times.getStop();
                if (stop_id == stop) {
                    Trip trip_id = times.getTrip();
                    Route route = trip_id.getRoute();

                    LocalTime arrival_time = LocalTime.ofSecondOfDay(times.getArrivalTime());
                    String hour = String.valueOf(arrival_time.getHour());
                    String minute = String.valueOf(arrival_time.getMinute());
                    int calculation = times.getArrivalTime() - Current_time.toSecondOfDay();
                    int relative_time = abs((Current_time.toSecondOfDay() - times.getArrivalTime()) / 60);
                    String time = "";

                    if (calculation <= 7200 && calculation >= 0) {
                        String route_name = route.getShortName();
                        //System.out.println(Integer.parseInt(route_name));

                        if (Objects.equals(oblika_casa, "absolute")) {
                            time = hour + ":" + minute;
                        } else if (Objects.equals(oblika_casa, "relative")) {
                            time = relative_time + "min";
                        }

                        int index = routes.indexOf(route_name);

                        if (index == -1) {
                            routes.add(route_name);
                            ArrayList<String> _times = new ArrayList<>();
                            _times.add(time);
                            route_times.add(_times);
                        } else {
                            if (route_times.get(index).size() < Integer.parseInt(st_nasljednjih_postaj)) {
                                route_times.get(index).add(time);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < routes.size(); i++) {
                System.out.println(routes.get(i) + ": " + String.join(",", route_times.get(i)));
            }
        } else {
            System.err.println("id_postaje st.nasljednjih_postaj relative/absolute");
            System.exit(-1);
        }
    }
}
