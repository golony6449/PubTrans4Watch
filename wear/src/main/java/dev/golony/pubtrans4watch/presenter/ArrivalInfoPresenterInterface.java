package dev.golony.pubtrans4watch.presenter;

import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.view.ArrivalInfo;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;

public interface ArrivalInfoPresenterInterface {
    interface View {
        void setResponseData(ArrivalInfo arrivalInfo);
    }

    interface Presenter {
        void setDataFromTopis(ArrivalInfoAdaptor.ViewHolder holder, Position position);
    }
}
