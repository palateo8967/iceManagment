# 📦 data/

Esta carpeta contiene todo lo relacionado con el manejo de datos de la aplicación. 
Aquí se definen las fuentes de datos, los modelos y los repositorios que interactúan con Room (local) o Firebase (remoto).

## Subcarpetas:

- **local/**: Definiciones de entidades y DAOs para Room (SQLite).
- **remote/**: Clases que se conectan con Firebase (Firestore, Auth, etc.).
- **model/**: Clases de datos como Venta, Pedido, Producto.
- **repository/**: Lógica para acceder y combinar los datos, usando local y/o remoto.
