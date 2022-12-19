package Lab4_5;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {
	private static final long serialVersionUID = 1L;

	private ArrayList<Double[]> graphicsData;
    private ArrayList<Double[]> originalData;
	
    private boolean showAxis = true;
	private boolean showMarkers = true;
    
	private int selectedMarker = -1;
    
	private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    
    private double[][] viewport = new double[2][2];
    private ArrayList<double[][]> undoHistory = new ArrayList<double[][]>(5);
    private double scaleX;
    private double scaleY;
    
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke gridStroke;
    private BasicStroke markerStroke;
    private BasicStroke selectionStroke;
    
    private Font axisFont;
    private Font labelsFont;
    
    private static DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    private boolean scaleMode = false;
    private boolean changeMode = false;
   
    private double[] originalPoint = new double[2];
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
		graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {8, 2, 2, 2, 2, 2, 4, 2, 4, 2}, 0.0f);
		axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
		gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
		markerStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        selectionStroke = new BasicStroke(1.0F, 0, 0, 10.0F, new float[]{10.0F, 10.0F}, 0.0F);
        axisFont = new Font("Serif", 1, 26);
        labelsFont = new Font("Serif", 1, 10);
        formatter.setMaximumFractionDigits(5);
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }

    public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
		repaint();
	}

	public void setShowMarkers(boolean showMarkers) {
		this.showMarkers = showMarkers;
		repaint();
	}
	
    public void displayGraphics(ArrayList<Double[]> graphicsData) {
        this.graphicsData = graphicsData;
        this.originalData = new ArrayList<Double[]>(graphicsData.size());
        Iterator<Double[]> dataPoints = graphicsData.iterator();

        while(dataPoints.hasNext()) {
            Double[] point = (Double[])dataPoints.next();
            Double[] newPoint = new Double[]{(Double)point[0], (Double)point[1]};
            originalData.add(newPoint);
        }

        minX = ((Double[])graphicsData.get(0))[0];
        maxX = ((Double[])graphicsData.get(graphicsData.size() - 1))[0];
        minY = ((Double[])graphicsData.get(0))[1];
        maxY = minY;

        for(int i = 1; i < graphicsData.size(); ++i) {
            if (((Double[])graphicsData.get(i))[1] < minY) {
                minY = ((Double[])graphicsData.get(i))[1];
            }

            if (((Double[])graphicsData.get(i))[1] > maxY) {
                maxY = ((Double[])graphicsData.get(i))[1];
            }
        }

        zoomToRegion(minX, maxY, maxX, minY);
    }

    public void zoomToRegion(double x1, double y1, double x2, double y2) {
        viewport[0][0] = x1;
        viewport[0][1] = y1;
        viewport[1][0] = x2;
        viewport[1][1] = y2;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        scaleX = getSize().getWidth() / (viewport[1][0] - viewport[0][0]);
        scaleY = getSize().getHeight() / (viewport[0][1] - viewport[1][1]);
        if (graphicsData != null && graphicsData.size() != 0) {
            Graphics2D canvas = (Graphics2D)g;
            paintGrid(canvas);
            if (showAxis)
    			paintAxis(canvas);

    		paintGraphics(canvas);
    		
    		if (showMarkers)
    			paintMarkers(canvas);
            paintLabels(canvas);
            paintSelection(canvas);
        }
    }

    private void paintSelection(Graphics2D canvas) {
        if (scaleMode) {
            canvas.setStroke(selectionStroke);
            canvas.setColor(Color.BLACK);
            canvas.draw(selectionRect);
        }
    }

    private void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.DARK_GRAY);
        Double currentX = null;
        Double currentY = null;
        Iterator<Double[]> dataPoints = graphicsData.iterator();

        while(dataPoints.hasNext()) {
            Double[] point = (Double[])dataPoints.next();
            if (point[0] >= viewport[0][0] && point[1] <= viewport[0][1] && 
            		point[0] <= viewport[1][0] && point[1] >= viewport[1][1]) {
                if (currentX != null && currentY != null) {
                    canvas.draw(new Line2D.Double(translateXYtoPoint(currentX, currentY), 
                    		translateXYtoPoint(point[0], point[1])));
                }
                currentX = point[0];
                currentY = point[1];
            }
        }
    }
    
    protected boolean isIncrease(Double pointValue, Double prevPointValue) {
		if(pointValue > prevPointValue)
			return true;
		return false;
	}

    private void paintMarkers(Graphics2D canvas) {
    	canvas.setStroke(markerStroke);
		canvas.setColor(Color.RED);
		Double prevPointValueY = 0.0;
        int i = -1;
        Iterator<Double[]> dataPoints = graphicsData.iterator();

        while(dataPoints.hasNext()) {
            Double[] point = (Double[])dataPoints.next();
            ++i;
            if(i != 0) {
				if(isIncrease(point[1], prevPointValueY)) {
					canvas.setColor(Color.GREEN);
				} else canvas.setColor(Color.RED);
			}
            Ellipse2D.Double circle = new Ellipse2D.Double();
			
			Point2D.Double center = translateXYtoPoint(point[0], point[1]);
			Point2D.Double corner = shiftPoint(center, 5, 5);
			circle.setFrameFromCenter(center, corner);
			
			canvas.draw(circle); 
			canvas.draw(new Line2D.Double(shiftPoint(center, 5, 0), shiftPoint(center, -5, 0)));
			canvas.draw(new Line2D.Double(shiftPoint(center, 0, 5), shiftPoint(center, 0, -5)));
            prevPointValueY = point[1];
        }
    }

    private void paintLabels(Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        canvas.setFont(this.labelsFont);
        FontRenderContext context = canvas.getFontRenderContext();
        Point2D.Double point;
        String label;
        Rectangle2D bounds;
        if (this.selectedMarker >= 0) {
            point = translateXYtoPoint(((Double[])graphicsData.get(selectedMarker))[0], 
            		((Double[])graphicsData.get(selectedMarker))[1]);
            label = "(" + formatter.format(((Double[])graphicsData.get(selectedMarker))[0]) + "," + 
            		formatter.format(((Double[])graphicsData.get(selectedMarker))[1]) + ")";
            bounds = labelsFont.getStringBounds(label, context);
            canvas.drawString(label, (float)(point.getX() + 5.0D), (float)(point.getY() - bounds.getHeight()));
        }
    }
    
    protected void paintGrid(Graphics2D canvas) {
		canvas.setStroke(gridStroke);
		canvas.setColor(Color.GRAY);
		
		int numOfSteps = 10;
		paintVerticalGrid(canvas, numOfSteps);
		paintHorizontalGrid(canvas, numOfSteps);
	}

	private void paintVerticalGrid(Graphics2D canvas, int numOfSteps) {
		double stepX = (maxX - minX) / numOfSteps;
		double fromX;
		for(fromX = 0.0; fromX < maxX; fromX += stepX) {
			canvas.draw(new Line2D.Double(translateXYtoPoint(fromX, minY), translateXYtoPoint(fromX, maxY)));
		}
		
		if(minX < 0) {
			for(fromX = 0.0; fromX > minX; fromX -= stepX) {
				canvas.draw(new Line2D.Double(translateXYtoPoint(fromX, minY), translateXYtoPoint(fromX, maxY)));
			}
		}
	}
	
	private void paintHorizontalGrid(Graphics2D canvas, int numOfSteps) {
		double stepY = (maxY - minY) / numOfSteps;
		double fromY;		
		for(fromY = 0.0; fromY < maxX; fromY += stepY) {
			canvas.draw(new Line2D.Double(translateXYtoPoint(maxX, fromY), translateXYtoPoint(minX, fromY)));
		}
		
		if(minX < 0) {
			for(fromY = 0.0; fromY > minY; fromY -= stepY) {
				canvas.draw(new Line2D.Double(translateXYtoPoint(maxX, fromY), translateXYtoPoint(minX, fromY)));
			}
		}
	}
	
	protected void paintAxis(Graphics2D canvas) {
		canvas.setStroke(axisStroke);
		canvas.setColor(Color.BLACK);
		canvas.setPaint(Color.BLACK);
		canvas.setFont(axisFont);
		FontRenderContext context = canvas.getFontRenderContext();

		canvas.draw(new Line2D.Double(translateXYtoPoint(0, maxY), translateXYtoPoint(0, minY)));

		GeneralPath arrowY = new GeneralPath();

		Point2D.Double lineEndY = translateXYtoPoint(0, maxY);
		arrowY.moveTo(lineEndY.getX(), lineEndY.getY());
		arrowY.lineTo(arrowY.getCurrentPoint().getX() + 3, arrowY.getCurrentPoint().getY() + 10);
		arrowY.lineTo(arrowY.getCurrentPoint().getX() - 6, arrowY.getCurrentPoint().getY());
		arrowY.closePath();

		canvas.draw(arrowY);
		canvas.fill(arrowY);
		Rectangle2D boundsY = axisFont.getStringBounds("y", context);
		Point2D.Double labelPosY = translateXYtoPoint(0, maxY);

		canvas.drawString("y", (float) labelPosY.getX() + 10, (float) (labelPosY.getY() - boundsY.getY()));
		canvas.draw(new Line2D.Double(translateXYtoPoint(minX, 0), translateXYtoPoint(maxX, 0)));

		
		GeneralPath arrowX = new GeneralPath();

		Point2D.Double lineEndX = translateXYtoPoint(maxX, 0);
		arrowX.moveTo(lineEndX.getX(), lineEndX.getY());
		arrowX.lineTo(arrowX.getCurrentPoint().getX() - 10, arrowX.getCurrentPoint().getY() - 3);
		arrowX.lineTo(arrowX.getCurrentPoint().getX(), arrowX.getCurrentPoint().getY() + 6);
		arrowX.closePath();

		canvas.draw(arrowX);
		canvas.fill(arrowX);
		Rectangle2D boundsX = axisFont.getStringBounds("x", context);
		Point2D.Double labelPosX = translateXYtoPoint(maxX, 0);
		canvas.drawString("x", (float) (labelPosX.getX() - boundsX.getWidth() - 10),
				(float) (labelPosX.getY() + boundsX.getY()));
	}

    protected Point2D.Double translateXYtoPoint(double x, double y) {
        double deltaX = x - this.viewport[0][0];
        double deltaY = this.viewport[0][1] - y;
        return new Point2D.Double(deltaX * this.scaleX, deltaY * this.scaleY);
    }

    protected double[] xyToPoint(int x, int y) {
        return new double[]{viewport[0][0] + (double)x / scaleX, viewport[0][1] - (double)y / scaleY};
    }
        
	protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
		Point2D.Double dest = new Point2D.Double();
		dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
		return dest;
	}

    protected int getNumSelectedPoint(int x, int y) {
        if (this.graphicsData == null) {
            return -1;
        } else {
            int pos = 0;
            for(Iterator<Double[]> dataPoints = graphicsData.iterator(); dataPoints.hasNext(); ++pos) {
                Double[] point = (Double[])dataPoints.next();
                java.awt.geom.Point2D.Double screenPoint = translateXYtoPoint(point[0], point[1]);
                double distance = (screenPoint.getX() - (double)x) * (screenPoint.getX() - 
                		(double)x) + (screenPoint.getY() - (double)y) * (screenPoint.getY() - (double)y);
                if (distance < 100.0D) {
                    return pos;
                }
            }
            return -1;
        }
    }

    public void reset() {
        this.displayGraphics(this.originalData);
    }

    public class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent ev) {
            if (ev.getButton() == 3) {
                if (undoHistory.size() > 0) {
                    viewport = (double[][])undoHistory.get(undoHistory.size() - 1);
                    undoHistory.remove(undoHistory.size() - 1);
                } else {
                    zoomToRegion(minX, maxY, maxX, minY);
                }
                repaint();
            }
        }

        public void mousePressed(MouseEvent ev) {
            if (ev.getButton() == 1) {
                selectedMarker = getNumSelectedPoint(ev.getX(), ev.getY());
                originalPoint = xyToPoint(ev.getX(), ev.getY());
                if (selectedMarker >= 0) {
                    changeMode = true;
                } else {
                    scaleMode = true;
                    selectionRect.setFrame((double)ev.getX(), (double)ev.getY(), 1.0D, 1.0D);
                }
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.getButton() == 1) {
                if (changeMode) {
                    changeMode = false;
                } else {
                    scaleMode = false;
                    double[] finalPoint = xyToPoint(ev.getX(), ev.getY());
                    undoHistory.add(viewport);
                    viewport = new double[2][2];
                    zoomToRegion(originalPoint[0], originalPoint[1], finalPoint[0], finalPoint[1]);
                    repaint();
                }
            }
        }
    }

    public class MouseMotionHandler implements MouseMotionListener {

        public void mouseMoved(MouseEvent ev) {
            selectedMarker = GraphicsDisplay.this.getNumSelectedPoint(ev.getX(), ev.getY());
            repaint();
        }

        public void mouseDragged(MouseEvent ev) {
            if (changeMode) {
                double[] currentPoint = xyToPoint(ev.getX(), ev.getY());
                double newY = ((Double[])graphicsData.get(selectedMarker))[1] + 
                		(currentPoint[1] - ((Double[])graphicsData.get(selectedMarker))[1]);
                if (newY > viewport[0][1]) {
                    newY = viewport[0][1];
                }

                if (newY < viewport[1][1]) {
                    newY = viewport[1][1];
                }

                ((Double[])graphicsData.get(selectedMarker))[1] = newY;
                repaint();
            } else {
                double width = (double)ev.getX() - selectionRect.getX();
                if (width < 5.0D) {
                    width = 5.0D;
                }

                double height = (double)ev.getY() - selectionRect.getY();
                if (height < 5.0D) {
                    height = 5.0D;
                }

                selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
                repaint();
            }

        }
    }
    
}
