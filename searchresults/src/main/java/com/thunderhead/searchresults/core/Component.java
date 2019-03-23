package com.thunderhead.searchresults.core;

import android.content.Context;

import com.thunderhead.searchresults.modules.ServicesModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {
        ServicesModule.class
})

/*
  Provides Initializer class to create new instance of the a Component.
  @see Component.Initializer
 */
public interface Component extends SearchResultGraph {

    /**
     * Provides init method to create new instance of the a Component.
     */
    final class Initializer {

        private Initializer() {
        }

        /**
         * Creates new instance of component.
         *
         * @param mockMode if API service should be mocked
         * @param context  Context calling method
         * @return new instance of Component
         */
        public static Component init(boolean mockMode, Context context) {
            return DaggerComponent.builder()
                    .servicesModule(new ServicesModule(mockMode, context))
                    .build();
        }
    }
}
