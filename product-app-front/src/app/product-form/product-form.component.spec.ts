import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductFormComponent } from './product-form.component';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, ProductFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form fields from currentProduct', () => {
    component.currentProduct = {
      id: 1,
      name: 'Test Product',
      description: 'Test Desc',
      price: 100,
      expirationDate: new Date('2025-12-31')
    };
    component.ngOnChanges();
    fixture.detectChanges();

    expect(component.name).toBe('Test Product');
    expect(component.description).toBe('Test Desc');
    expect(component.price).toBe(100);
    expect(component.expirationDate?.toISOString()).toContain('2025-12-31');
  });

  it('should call addProduct on submit if currentProduct is null', () => {
    const addSpy = spyOn(component, 'addProduct');
    component.name = 'New Product';
    component.description = 'New Desc';
    component.price = 50;
    component.expirationDate = new Date('2025-06-01');

    const form = fixture.debugElement.query(By.css('form'));
    form.triggerEventHandler('submit', { preventDefault: () => {} });

    expect(addSpy).toHaveBeenCalledOnceWith({
      name: 'New Product',
      description: 'New Desc',
      price: 50,
      expirationDate: new Date('2025-06-01')
    });
  });

  it('should call saveEditedProduct if currentProduct has an id', () => {
    const saveSpy = spyOn(component, 'saveEditedProduct');
    component.currentProduct = { id: 99 };
    component.name = 'Edited Product';
    component.description = 'Edited Desc';
    component.price = 200;
    component.expirationDate = new Date('2025-01-01');

    const form = fixture.debugElement.query(By.css('form'));
    form.triggerEventHandler('submit', { preventDefault: () => {} });

    expect(saveSpy).toHaveBeenCalledOnceWith({
      id: 99,
      name: 'Edited Product',
      description: 'Edited Desc',
      price: 200,
      expirationDate: new Date('2025-01-01')
    });
  });

  it('should call cleanCurrentProduct and reset form on cancel', () => {
    const cancelSpy = spyOn(component, 'cleanCurrentProduct');
    component.name = 'Test';
    component.description = 'Test';
    component.price = 99;
    component.expirationDate = new Date();

    const button = fixture.debugElement.query(By.css('button.btn-outline-danger'));
    button.triggerEventHandler('click', null);

    expect(cancelSpy).toHaveBeenCalled();
    expect(component.name).toBe('');
    expect(component.description).toBe('');
    expect(component.price).toBeNull();
    expect(component.expirationDate).toBeNull();
  });

  it('should alert when required fields are missing on submit', () => {
    spyOn(window, 'alert');
    component.name = '';
    component.description = '';
    component.price = null;

    const form = fixture.debugElement.query(By.css('form'));
    form.triggerEventHandler('submit', { preventDefault: () => {} });

    expect(window.alert).toHaveBeenCalledWith('Please fill in all required fields before saving.');
  });
});
