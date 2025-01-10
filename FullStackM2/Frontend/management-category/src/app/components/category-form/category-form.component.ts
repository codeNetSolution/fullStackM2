import { Component, OnInit , Input, Output, EventEmitter} from '@angular/core';
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
  @Input() categoryId?: number;
  @Output() formSubmit = new EventEmitter<void>();
  category: Category = new Category();

  constructor(
    private categoryService: CategoryService,
    private router: Router, 
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (this.categoryId) {
      this.categoryService.getCategoryDetails(this.categoryId).subscribe(
        (category) => {
          this.category = category;
          console.log("Catégorie chargée :", this.category);
        },
        (error) => console.error('Erreur lors du chargement de la catégorie', error)
      );
    }
  }


  saveCategory(): void {
    console.log("id is", this.category.id);

    if (this.category.root) {
      this.category.parentId = null;
    }
    
    if (this.category.id) {
     
      this.categoryService.updateCategory(this.category.id, this.category).subscribe(
        (response) => {
          console.log('Catégorie mise à jour avec succès', response);
          this.category = new Category();
          this.formSubmit.emit();
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
