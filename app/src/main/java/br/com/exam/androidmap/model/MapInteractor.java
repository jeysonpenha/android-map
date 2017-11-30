package br.com.exam.androidmap.model;

import br.com.exam.androidmap.presenter.MapPresenter;

public interface MapInteractor {

    void init(MapPresenter presenter);

    void searchAddress(String name);

    void initFavoriteList();

}
