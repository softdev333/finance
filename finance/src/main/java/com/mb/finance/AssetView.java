package com.mb.finance;

import static com.mb.finance.config.Constants.USER_ID;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mb.finance.entities.Asset;
import com.mb.finance.service.AssetService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "showasset", layout = MainLayout.class)
@PageTitle("Finance : Assets")
public class AssetView extends VerticalLayout implements BeforeEnterObserver {

    H2 pageTitle = new H2("All Assets");
    Grid<Asset> assetGrid = new Grid<>(Asset.class);

    @Override
    public void beforeEnter(BeforeEnterEvent arg0) {
	if (StringUtils.isEmpty((String) VaadinSession.getCurrent().getAttribute(USER_ID))) {
	    Notification errorNotification = new Notification("No user. Proceed to login", 3000, Position.MIDDLE);
	    errorNotification.open();
	    arg0.rerouteTo(LoginView.class);
	}
    }

    public AssetView(AssetService assetService) {
	String userId = (String) VaadinSession.getCurrent().getAttribute(USER_ID);
	List<Asset> assets = assetService.findByUserId(userId);
	assets.forEach(e -> {
	    e.setCurrentPrice(e.getCostPrice());
	});

	assetGrid.setColumns("name", "assetType", "costPrice", "currentPrice", "quantity", "purchaseDate", "comments");
	assetGrid.setWidth("70%");
	updateGridColumnSize();
	updateAssetGrid(assets);

	add(pageTitle, assetGrid);
    }

    public void updateGridColumnSize() {
	assetGrid.getColumnByKey("name").setAutoWidth(true);
	assetGrid.getColumnByKey("costPrice").setAutoWidth(true);
	assetGrid.getColumnByKey("assetType").setAutoWidth(true);
	assetGrid.getColumnByKey("comments").setAutoWidth(true);
	assetGrid.getColumnByKey("purchaseDate").setAutoWidth(true);
	assetGrid.getColumnByKey("quantity").setAutoWidth(true);
	assetGrid.getColumnByKey("currentPrice").setAutoWidth(true);
    }

    public void updateAssetGrid(List<Asset> assetList) {
	assetGrid.setItems(assetList);
	updateGridColumnSize();
    }

    public void modifyObject(Asset asset) {

    }

}
