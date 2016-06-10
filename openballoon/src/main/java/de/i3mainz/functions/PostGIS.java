package de.i3mainz.functions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * CLASS PostGIS
 *
 * @author Martin Unold M.Sc.
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
public class PostGIS {

	private final Connection connection;

	public PostGIS() throws IOException, ClassNotFoundException, SQLException {
		Properties config = new Properties();
		config.load(PostGIS.class.getClassLoader().getResourceAsStream("config.properties"));
		//Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://" + config.getProperty("host") + ":" + config.getProperty("port") + "/" + config.getProperty("database");
		connection = DriverManager.getConnection(url, config.getProperty("user"), config.getProperty("password"));
	}

	public void close() throws SQLException {
		connection.close();
	}

	public boolean executeSQL(String filename) throws FileNotFoundException, SQLException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(PostGIS.class.getClassLoader().getResource(filename).getFile()));
		String line;
		String sql = "";
		while ((line = reader.readLine()) != null) {
			sql += line;
		}
		return connection.createStatement().execute(sql);
	}

	public int insertEvent(String name, String location, double lon, double lat, Timestamp timestamp, int balloons) throws SQLException {
		String sql = "INSERT INTO Event(event_name,event_timestamp,location,geom) VALUES (?,?,?,ST_GeomFromText(?,4326))";
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, name);
		statement.setTimestamp(2, timestamp);
		statement.setString(3, location);
		statement.setString(4, "POINT(" + lon + " " + lat + ")");

		if (statement.executeUpdate() == 0) {
			return 0;
		}
		ResultSet resultSet = statement.getGeneratedKeys();
		if (!resultSet.next()) {
			return 0;
		}
		int id = resultSet.getInt(1);

		int result = 0;
		for (int i = 0; i < balloons; i++) {
			int nr = (int) (Utils.MAX_NR * Math.random());
			sql = "INSERT INTO Balloon(IDREF_event,nr) VALUES (?,?)";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.setInt(2, nr);
			result += statement.executeUpdate();
		}
		return result;
	}

	public int insertFind(int nr, String location, double lon, double lat, Timestamp timestamp, String remark) throws SQLException {
		String sql = "SELECT id FROM Balloon WHERE nr = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, nr);
		ResultSet resultSet = statement.executeQuery();
		if (!resultSet.next()) {
			return -1;
		}
		int id = resultSet.getInt(1);

		sql = "SELECT * FROM Find WHERE IDREF_balloon = ?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return -2;
		}

		sql = "INSERT INTO Find(IDREF_balloon,find_timestamp,location,geom,remark) VALUES (?,?,?,ST_GeomFromText(?,4326),?)";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, id);
		statement.setTimestamp(2, timestamp);
		statement.setString(3, location);
		statement.setString(4, "POINT(" + lon + " " + lat + ")");
		statement.setString(5, remark);
		return statement.executeUpdate();
	}

	public int deleteEvent(String name) throws SQLException {
		String sql = "DELETE FROM Event WHERE event_name = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, name);
		return statement.executeUpdate();
	}

	public ResultSet getEvent(String name) throws SQLException {
		String sql = "SELECT * FROM Event WHERE event_name = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, name);
		return statement.executeQuery();
	}

	public ResultSet getBalloons(int event) throws SQLException {
		String sql = "SELECT * FROM Balloon WHERE IDREF_event = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, event);
		return statement.executeQuery();
	}

	public ResultSet getFinds(int balloon) throws SQLException {
		String sql = "SELECT * FROM Find WHERE IDREF_balloon = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, balloon);
		return statement.executeQuery();
	}

	public ResultSet getBalloonData(int code) throws SQLException {
		String sql = "SELECT Event.event_name, ST_X(Find.geom), ST_Y(Find.geom) FROM Event, Balloon, Find WHERE Balloon.nr = ? AND Balloon.IDREF_event = Event.id AND Find.IDREF_balloon = Balloon.id";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, code);
		return statement.executeQuery();
	}
}
