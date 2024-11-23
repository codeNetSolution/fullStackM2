import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router , ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-category-form',
  standalone: true,
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css'],
  imports: [FormsModule, CommonModule]
})
export class CategoryFormComponent implements OnInit {
  category: Category = new Category();

  constructor(
    private categoryService: CategoryService,
    private router: Router, 
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if(id) {
        console.log("id from URL is", id);
       this.categoryService.getCategoryDetails(id).subscribe(
        category => {
          this.category = category;
          console.log("id is", this.category.id);
        }
       )
      }
    });
  }


  saveCategory(): void {
    console.log("id is", this.category.id);
    if (this.category.id) {
     
      this.categoryService.updateCategory(this.category.id, this.category).subscribe(
        (response) => {
          console.log('Catégorie mise à jour avec succès', response);
          this.category = new Category();
          this.router.navigate(['/categories']);
        },
        (error) => {
          console.error('Erreur lors de la mise à jour de la catégorie', error);
        }
      );
    } else {
      this.categoryService.createCategory(this.category).subscribe(
        (response) => {
          console.log('Catégorie créée avec succès', response);
          this.category = new Category(); 
          this.router.navigate(['/categories']);
        },
        (error) => {
          console.error('Erreur lors de la création de la catégorie', error);
        }
      );
    }
  }
  
}
