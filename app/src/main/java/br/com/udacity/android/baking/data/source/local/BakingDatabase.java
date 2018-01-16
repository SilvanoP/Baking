/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.udacity.android.baking.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.Step;

/**
 * The Room Database that contains the Recipe table.
 */
@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class BakingDatabase extends RoomDatabase {

    private static BakingDatabase INSTANCE;

    public abstract RecipesDao recipesDao();

    public abstract StepsDao stepsDao();

    public abstract IngredientsDao ingredientsDao();

    private static final Object sLock = new Object();

    public static BakingDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        BakingDatabase.class, "Baking.db")
                        .fallbackToDestructiveMigration() //TODO: remove it before release
                        .build();
            }
            return INSTANCE;
        }
    }

}
