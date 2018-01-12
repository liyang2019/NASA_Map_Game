package ly15_qz18.game.map;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwindx.examples.ClickAndGoSelectListener;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.ToolTipController;

/**
 * NASA Map Panel.
 *
 */
public class MapPanel extends JPanel{

	/**
	 * Compiler generated unique ID
	 */
	private static final long serialVersionUID = -3664035354316287136L;
	
	private IMap2ModelApapter map2ModelApapter;
	
	/**
	 * Actual Map Canvas
	 */
	private WorldWindowGLCanvas _wwd = new WorldWindowGLCanvas();
	
	/**
	 * @return the internal WorldWindowGLCanvas object
	 */
	public WorldWindowGLCanvas getWWD() {
		return _wwd;
	}
		
	/**
	 * Initialize configuration options for WorldWind to operate correctly.
	 * @param globeTypeClass 
	 */
	protected void initconfig(Class<? extends Globe> globeTypeClass) {
		// Configuration.setValue(AVKey.GLOBE_CLASS_NAME, EarthFlat.class.getName());
		Configuration.setValue(AVKey.GLOBE_CLASS_NAME, globeTypeClass.getName());
		Configuration.setValue(AVKey.VIEW_CLASS_NAME,
				BasicOrbitView.class.getName());
		Configuration
				.setValue(
						AVKey.LAYERS_CLASS_NAMES,
						"gov.nasa.worldwind.layers.CompassLayer,"
						//continents' name
//								+ "gov.nasa.worldwind.layers.Earth.NASAWFSPlaceNameLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.BMNGOneImage,"
								+ "gov.nasa.worldwind.layers.Earth.BMNGWMSLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.USDANAIPWMSImageLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.UTMGraticuleLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.LandsatI3WMSLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.USGSUrbanAreaOrtho,"
//								+ "gov.nasa.worldwind.layers.Earth.MSVirtualEarthLayer,"
//								+ "gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer,"   // Careful, this layer can get hidden by the other layers.
								);

		System.setProperty("java.net.useSystemProxies", "true");
		if (Configuration.isMacOS()) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name",
					"World Wind Application");
			System.setProperty("com.apple.mrj.application.growbox.intrudes",
					"false");
			System.setProperty("apple.awt.brushMetalLook", "true");
		} else if (Configuration.isWindowsOS()) {
			// Prevent flashing during window resizing
			System.setProperty("sun.awt.noerasebackground", "true");
		}
	}
	
	/**
	 * Setup the WorldWindow.
	 */
	protected void setupWWD() {
		// Create the default model as described in the current worldwind
		// properties.
		Model worldModel = (Model) WorldWind
				.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		getWWD().setModel(worldModel);

		// Deal with rendering exceptions - graphics not good enough
		getWWD().addRenderingExceptionListener(new RenderingExceptionListener() {
			public void exceptionThrown(Throwable t) {
				if (t instanceof WWAbsentRequirementException) {
					String message = "Computer does not meet minimum graphics requirements.\n";
					message += "Please install up-to-date graphics driver and try again.\n";
					message += "Reason: " + t.getMessage() + "\n";
					message += "This program will end when you press OK.";

					JOptionPane.showMessageDialog(MapPanel.this, message,
							"Unable to Start Program",
							JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
			}
		});

		// Setup a select listener for the worldmap click-and-go feature
		getWWD().addSelectListener(new ClickAndGoSelectListener(getWWD(),
				WorldMapLayer.class));

		// Put the world window in the center
		add(getWWD(), BorderLayout.CENTER);
	}
	
	/**
	 * Add a layer of annotations to the map.
	 * Layer is added below the compass layer.
	 * 
	 * @param layer layer to add to the map
	 */
	public void addLayer(Layer layer) {
		insertBeforeCompass(layer);
	}
	
	

	/**
	 * Remove a layer from the map
	 * 
	 * @param layer layer to remove from the map
	 */
	public void removeLayer(Layer layer) {
		LayerList layers = getWWD().getModel().getLayers();
		layers.remove(layer);
	}
	
	/**
	 * Accessor for the current altitude (eye point)
	 * @param currentAltitude the currentAltitude to set
	 */
	protected void setCurrentAltitude(double currentAltitude) {
	}
	
	/**
	 * Set up the status bar, which shows latitude, longitude, altitude, etc.
	 */
	protected void setupStatusBar() {
		StatusBar statusBar = new StatusBar() {
			private static final long serialVersionUID = -8334549384792867496L;

			@Override
			protected String makeAngleDescription(String label, Angle angle) {
				String s;
				if (Angle.ANGLE_FORMAT_DMS.equals(getAngleFormat()))
					s = String.format("%s %s", label, angle.toDMSString());
				else
					s = String.format("%s %,.6f\u00B0", label, angle.degrees);
				return s;
			}
			
			/**
			 * Overridden to capture the current altitude.
			 */
			@Override
			protected String makeEyeAltitudeDescription(double metersAltitude){
				//System.out.println("metersAltitude: "+ metersAltitude);
				setCurrentAltitude(metersAltitude);
				return super.makeEyeAltitudeDescription(metersAltitude);
			}
		};
		add(statusBar, BorderLayout.PAGE_END);
		statusBar.setEventSource(getWWD());
	}
	
	/**
	 * Insert layer just beneath the Compass layer.
	 * 
	 * @param layer
	 *            - layer to insert
	 */
	protected void insertBeforeCompass(Layer layer) {
		// Insert the layer into the layer list just before the compass.
		int compassPosition = 0;
		LayerList layers = getWWD().getModel().getLayers();
		for (Layer l : layers) {
			if (l instanceof CompassLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition, layer);
	}
	
	/**
	 * Setup the controllers in the view which allow navigation around the map.
	 */
	protected void setupViewControllers() {
		ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
		@SuppressWarnings("unused")
		ToolTipController toolTipController = new ToolTipController(getWWD(),
				AVKey.DISPLAY_NAME, null);
		@SuppressWarnings("unused")
		HighlightController highlightController = new HighlightController(getWWD(),
				SelectEvent.ROLLOVER);

		insertBeforeCompass(viewControlsLayer);
		getWWD().addSelectListener(new ViewControlsSelectListener(getWWD(),
				viewControlsLayer));
	}
	
	/**
	 * Convert gradient sky to color sky.
	 */
	protected void setupSky() {
		// Change atmosphere SkyGradientLayer for SkyColorLayer
		LayerList layers = getWWD().getModel().getLayers();
		for (int i = 0; i < layers.size(); i++) {
			if (layers.get(i) instanceof SkyGradientLayer)
				layers.set(i, new SkyColorLayer());
		}
	}

	/**
	 * COnstructor.
	 * @param globeTypeClass - the globe type class.
	 */
	public MapPanel(Class<? extends Globe> globeTypeClass){
		super(new BorderLayout());
		/**
		 * The following setPreferredSize(new Dimension(0,0)) is a work around
		 * to a known JOGL problem when working with resizeable elements, e.g. JScrollPanes.
		 * See http://jogamp.org/jogl/doc/userguide/ and look under the 
		 * "Heavyweight and Lightweight Issues" section.
		 * Without this line, if this panel is put into a JScrollPane, when the scroll pane
		 * size is _reduced_ and the scroll bars appear, the map will not scroll 
		 * properly; simply shifting in space on top of other elements.   The displayed
		 * map is also incorrect, an objects that should be visible on the edges may not 
		 * be visible.   The problem does not appear when the scroll pane size is increased.   With the
		 * following line, the map will always be the same size of the scroll pane's 
		 * viewport and thus the scroll bars will never appear.
		 */
		setPreferredSize(new Dimension(0,0)); 
		initconfig(globeTypeClass);
		setSelectListener();
	}
	
	/**
	 * Set select listener
	 */
	public void setSelectListener() {
		getWWD().addSelectListener(new SelectListener() {
			
			@Override
			public void selected(SelectEvent event) {
				if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
					if (event.hasObjects()) {
						Object obj = event.getTopObject();
						if (obj instanceof CityAnnotation) {
							CityAnnotation annotation = (CityAnnotation) obj;							
							MapPanel.this.map2ModelApapter.sendLeftClick(annotation.getCity().getUUID());
						}
					}
				}
				if (event.getEventAction().equals(SelectEvent.RIGHT_CLICK)) {
					if (event.hasObjects()) {
						Object obj = event.getTopObject();
						if (obj instanceof CityAnnotation) {
							CityAnnotation annotation = (CityAnnotation) obj;
							System.out.println(System.currentTimeMillis() + "--right clicked city");							
							MapPanel.this.map2ModelApapter.sendRightClick(annotation.getCity().getUUID());
						}
					}
				}
			}
		});
	}
	
	/**
	 * constructor
	 * @param adapter IMap2ModelApapter
	 */
	public MapPanel(IMap2ModelApapter adapter) {
		this(Earth.class);
		this.map2ModelApapter = adapter;
	}
	
	/**
	 * start the game model
	 */
	public void start() {
		setupWWD();
		setupStatusBar();
		setupViewControllers();
		setupSky();		
		WWUtil.alignComponent(null, this, AVKey.CENTER);
	}

}
