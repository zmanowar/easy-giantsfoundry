package com.toofifty.easygiantsfoundry;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.Area;

public class FoundryWidgetOverlay extends Overlay {

    private static final int REGION_ID = 13491;
    private final Client client;
    private final EasyGiantsFoundryPlugin plugin;
    private final EasyGiantsFoundryState state;
    private final EasyGiantsFoundryHelper helper;
    private final EasyGiantsFoundryConfig config;

    private static final Color HIGHLIGHT_BORDER_COLOR = Color.ORANGE;
    private static final Color HIGHLIGHT_HOVER_BORDER_COLOR = HIGHLIGHT_BORDER_COLOR.darker();
    private static final Color HIGHLIGHT_FILL_COLOR = new Color(0, 255, 0, 20);

    @Inject
    private FoundryWidgetOverlay(Client client, EasyGiantsFoundryPlugin plugin, EasyGiantsFoundryState state, EasyGiantsFoundryHelper helper, EasyGiantsFoundryConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.state = state;
        this.helper = helper;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget selectedWidget = state.getSelectedWidget();
        if (selectedWidget != null) {
            highlightWidget(graphics, selectedWidget, selectedWidget.getParent());
        }
        return null;
    }

    void highlightWidget(Graphics2D graphics, Widget toHighlight, Widget container)
    {

        Rectangle padding = new Rectangle();

        Point canvasLocation = toHighlight.getCanvasLocation();

        if (canvasLocation == null)
        {
            return;
        }

        Point windowLocation = container.getCanvasLocation();

        if (windowLocation.getY() > canvasLocation.getY() + toHighlight.getHeight()
                || windowLocation.getY() + container.getHeight() < canvasLocation.getY())
        {
            return;
        }

        // Visible area of widget
        Area widgetArea = new Area(
                new Rectangle(
                        canvasLocation.getX() - padding.x,
                        Math.max(canvasLocation.getY(), windowLocation.getY()) - padding.y,
                        toHighlight.getWidth() + padding.x + padding.width,
                        Math.min(
                                Math.min(windowLocation.getY() + container.getHeight() - canvasLocation.getY(), toHighlight.getHeight()),
                                Math.min(canvasLocation.getY() + toHighlight.getHeight() - windowLocation.getY(), toHighlight.getHeight())) + padding.y + padding.height
                ));

        OverlayUtil.renderHoverableArea(graphics, widgetArea, client.getMouseCanvasPosition(),
                HIGHLIGHT_FILL_COLOR, HIGHLIGHT_BORDER_COLOR, HIGHLIGHT_HOVER_BORDER_COLOR);
    }

}
