import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { CategoryFormComponent } from './components/category-form/category-form.component';
import { CategoryDetailsComponent } from './components/category-details/category-details.component';



export const routes: Routes = [
    { path: '', redirectTo: '/categories', pathMatch:'full'},
    { path: 'categories', component: CategoryListComponent},
    { path: 'category/new', component: CategoryFormComponent},
    { path: 'category/edit/:id', component: CategoryFormComponent},
    { path: 'category/:id', component: CategoryDetailsComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
