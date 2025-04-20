import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;

public class busTrips {
    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
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

            for (Route route : store.getAllRoutes()){
                System.out.println("route:" + route.getShortName());
            }
        } else {
            System.err.println("id_postaje st.nasljednjih_postaj relative/absolute");
            System.exit(-1);
        }
    }
}
