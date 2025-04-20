import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.model.*;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class busTrips {
    public static <Int> void main(String[] args) throws IOException {
        if (args.length == 3) {
            LocalTime Current_time = LocalTime.now();

            String id_postaje = args[0];
            String st_nasljednjih_postaj = args[1];
            String oblika_casa = args[2];

            System.out.println(id_postaje);
            System.out.println(st_nasljednjih_postaj);
            System.out.println(oblika_casa);

            GtfsReader reader = new GtfsReader();
            reader.setInputLocation(new File("gtfs"));

            GtfsDaoImpl store = new GtfsDaoImpl();
            reader.setEntityStore(store);

            reader.run();

            Agency[] agency = store.getAllAgencies().toArray(new Agency[0]);
            AgencyAndId id_stop = AgencyAndId.convertFromString(agency[0].getId()+"_"+id_postaje);

            Stop stop = store.getStopForId(id_stop);
            String stop_name = stop.getName();
            System.out.println("Postajališče "+ stop_name);

            StopTime[] stop_times = store.getAllStopTimes().toArray(new StopTime[0]);
            ArrayList<AgencyAndId> trip_ids = new ArrayList<>();

            for (StopTime times : stop_times){
                StopLocation stop_id = times.getStop();
                if (stop_id == stop){
                    AgencyAndId trip_id = times.getTrip().getId();
                    LocalTime arrival_time = LocalTime.ofSecondOfDay(times.getArrivalTime());
                    int calculation = times.getArrivalTime() - Current_time.toSecondOfDay();

                    if(calculation <= 7200 && calculation >= 0){
                        System.out.println(arrival_time);
                    }
                }
            }

        } else {
            System.err.println("id_postaje st.nasljednjih_postaj relative/absolute");
            System.exit(-1);
        }
    }
}
