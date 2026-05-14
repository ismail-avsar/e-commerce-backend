# Bandage E-Commerce Backend

Spring Boot ile gelistirilmis REST API. Workintech e-commerce bitirme odevindeki T08-T23 issue akislarini ve React frontend uygulamasinin bekledigi endpoint/response formatlarini destekler.

## Canli Backend

Backend Render uzerinde Docker + PostgreSQL ile yayindadir:

```text
https://e-commerce-backend-tpne.onrender.com
```

Canli frontend Vercel uzerinden bu backend'e baglanir:

```text
https://e-commerce-two-fawn-25.vercel.app
```

## Proje Durumu

Temel e-commerce akislari tamamlanmistir:

- Kullanici kaydi ve girisi
- JWT token dogrulama
- Rol listeleme
- Kategori listeleme
- Urun listeleme, filtreleme, siralama ve sayfalama
- Urun detay bilgisi
- Adres CRUD islemleri
- Kredi karti CRUD islemleri
- Siparis olusturma
- Gecmis siparisleri listeleme

## Teknolojiler

| Katman | Teknoloji |
|---|---|
| Dil | Java 17 |
| Framework | Spring Boot 3.2.1 |
| Guvenlik | Spring Security, JWT, BCrypt |
| Veritabani | PostgreSQL, H2 test |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |
| Deployment | Render, Docker |

## Lokal Calistirma

### 1. PostgreSQL veritabani olustur

```sql
CREATE DATABASE bandage_ecommerce;
```

### 2. Ortam degiskenlerini ayarla

Gercek sifreler GitHub'a yuklenmez. Ortam degiskeni olarak verilir.

PowerShell:

```powershell
$env:DB_PASSWORD="kendi_postgresql_sifreniz"
$env:JWT_SECRET="uzun-ve-gizli-bir-jwt-anahtari"
```

Istege bagli olarak sunlar da degistirilebilir:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/bandage_ecommerce"
$env:DB_USERNAME="postgres"
$env:FRONTEND_ORIGIN="http://localhost:5173"
```

### 3. Uygulamayi baslat

```powershell
.\mvnw.cmd spring-boot:run
```

Backend varsayilan olarak su adreste calisir:

```text
http://localhost:8080
```

## Render Deployment

Backend Render Blueprint ile deploy edilir. Repo kokunde su dosyalar bulunur:

- `render.yaml`
- `Dockerfile`
- `.dockerignore`

`render.yaml`, Render uzerinde iki kaynak olusturur:

- `e-commerce-backend` web service
- `e-commerce-db` PostgreSQL database

Render tarafinda kullanilan baslica ortam degiskenleri:

```env
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
JWT_SECRET
FRONTEND_ORIGIN=https://e-commerce-two-fawn-25.vercel.app
```

Port ayari Render ile uyumludur:

```properties
server.port=${PORT:8080}
```

Veritabani URL'i hem manuel `DB_URL` ile hem de Render'in database degiskenleriyle calisacak sekilde ayarlanmistir.

## Test Kullanicilari

Uygulama ilk calistiginda seed data otomatik olusturulur:

| Email | Sifre | Rol |
|---|---|---|
| customer@commerce.com | 123456 | Customer |
| store@commerce.com | 123456 | Store |
| admin@commerce.com | 123456 | Admin |

## Token Kullanimi

Issue gereksinimlerine uygun olarak token, header icinde dogrudan gonderilir. `Bearer` prefix'i kullanilmaz.

```text
Authorization: JWT_TOKEN
```

## API Endpointleri

| Method | Endpoint | Aciklama |
|---|---|---|
| GET | `/roles` | Rolleri listeler |
| POST | `/signup` | Yeni kullanici olusturur |
| POST | `/login` | Kullanici girisi yapar |
| GET | `/verify` | Token dogrular, kullanici bilgisi doner |
| GET | `/categories` | Kategorileri listeler |
| GET | `/products` | Urunleri listeler |
| GET | `/products/{productId}` | Tek urun detayini getirir |
| GET | `/user/address` | Kullanici adreslerini listeler |
| POST | `/user/address` | Yeni adres ekler |
| PUT | `/user/address` | Adres gunceller |
| DELETE | `/user/address/{addressId}` | Adres siler |
| GET | `/user/card` | Kullanici kartlarini listeler |
| POST | `/user/card` | Yeni kart ekler |
| PUT | `/user/card` | Kart gunceller |
| DELETE | `/user/card/{cardId}` | Kart siler |
| POST | `/order` | Siparis olusturur |
| GET | `/order` | Gecmis siparisleri listeler |

## Urun Listeleme

`GET /products` su query parametrelerini destekler:

| Parametre | Aciklama |
|---|---|
| `category` | Kategori ID'ye gore filtreler |
| `filter` | Urun adi ve aciklamasinda metin aramasi yapar |
| `sort` | `price:asc`, `price:desc`, `rating:asc`, `rating:desc` |
| `limit` | Sayfa basina urun sayisi |
| `offset` | Kacinci urunden baslanacagi |

Ornek:

```text
GET /products?category=3&filter=gri&sort=rating:desc&limit=25&offset=0
```

Response formati:

```json
{
  "total": 25,
  "products": []
}
```

## Canli Test Edilen Akislar

Teslim oncesi canli ortamda su kontroller yapildi:

- `GET /categories`
- `GET /products?limit=2&offset=0`
- `POST /login`
- Frontend ile urun listeleme
- Frontend ile login
- Vercel frontend'in Render backend URL'ine baglanmasi

## Guvenlik Notlari

- Gercek veritabani sifresi GitHub'a yuklenmez.
- JWT secret GitHub'a yuklenmez.
- Kredi karti CVV bilgisi saklanmaz.
- Kart ve odeme akisi egitim/odev kapsaminda demo amaclidir.

## Yazar

Ismail Avsar - [@ismail-avsar](https://github.com/ismail-avsar)
