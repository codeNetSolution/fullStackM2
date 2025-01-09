import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { CategoryListComponent } from "./components/category-list/category-list.component";
import { CategoryFormComponent } from "./components/category-form/category-form.component";
import { CategoryDetailsComponent } from "./components/category-details/category-details.component";
import { HomePageComponent } from "./page/home-page/home-page.component";

const routes: Routes = [
    { path: '', component: HomePageComponent},
    { path: 'categories', component: CategoryListComponent },
    { path: 'category/new', component: CategoryFormComponent },
    { path: 'category/edit/:id', component: CategoryFormComponent },
    { path: 'category/:id', component: CategoryDetailsComponent}

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })

export class AppRoutingModule { }