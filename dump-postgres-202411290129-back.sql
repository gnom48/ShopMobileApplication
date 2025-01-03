PGDMP                     
    |            postgres     15.1 (Ubuntu 15.1-1.pgdg20.04+1)    15.2 n    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    5    postgres    DATABASE     p   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C.UTF-8';
    DROP DATABASE postgres;
                postgres    false            �           0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    4076            �           0    0    DATABASE postgres    ACL     2   GRANT ALL ON DATABASE postgres TO dashboard_user;
                   postgres    false    4076            �           0    0    postgres    DATABASE PROPERTIES     �   ALTER DATABASE postgres SET "app.settings.jwt_secret" TO 'your-super-secret-jwt-token-with-at-least-32-characters-long';
ALTER DATABASE postgres SET "app.settings.jwt_exp" TO '3600';
                     postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                pg_database_owner    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   pg_database_owner    false    20            �           0    0    SCHEMA public    ACL     �   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO postgres;
GRANT USAGE ON SCHEMA public TO anon;
GRANT USAGE ON SCHEMA public TO authenticated;
GRANT USAGE ON SCHEMA public TO service_role;
                   pg_database_owner    false    20            �           1247    33865    userkpilevelsorm    TYPE     j   CREATE TYPE public.userkpilevelsorm AS ENUM (
    'TRAINEE',
    'SPECIALIST',
    'EXPERT',
    'TOP'
);
 #   DROP TYPE public.userkpilevelsorm;
       public          postgres    false    20            �           1247    33874    userstatusesorm    TYPE     H   CREATE TYPE public.userstatusesorm AS ENUM (
    'OWNER',
    'USER'
);
 "   DROP TYPE public.userstatusesorm;
       public          postgres    false    20            �           1247    33880    usertypesorm    TYPE     M   CREATE TYPE public.usertypesorm AS ENUM (
    'COMMERCIAL',
    'PRIVATE'
);
    DROP TYPE public.usertypesorm;
       public          postgres    false    20            �           1247    33886    worktaskstypesorm    TYPE     �   CREATE TYPE public.worktaskstypesorm AS ENUM (
    'FLYERS',
    'CALLS',
    'SHOW',
    'MEET',
    'DEAL',
    'DEPOSIT',
    'SEARCH',
    'ANALYTICS',
    'OTHER',
    'DEAL_RENT',
    'DEAL_SALE'
);
 $   DROP TYPE public.worktaskstypesorm;
       public          postgres    false    20            �           1259    50762    buckets    TABLE     �   CREATE TABLE public.buckets (
    user_id character varying NOT NULL,
    product_example_id integer NOT NULL,
    quantity integer NOT NULL
);
    DROP TABLE public.buckets;
       public         heap    postgres    false    20            �           0    0    TABLE buckets    ACL     �   GRANT ALL ON TABLE public.buckets TO anon;
GRANT ALL ON TABLE public.buckets TO authenticated;
GRANT ALL ON TABLE public.buckets TO service_role;
          public          postgres    false    384            |           1259    34326 
   categories    TABLE     X   CREATE TABLE public.categories (
    id integer NOT NULL,
    name character varying
);
    DROP TABLE public.categories;
       public         heap    postgres    false    20            �           0    0    TABLE categories    ACL     �   GRANT ALL ON TABLE public.categories TO anon;
GRANT ALL ON TABLE public.categories TO authenticated;
GRANT ALL ON TABLE public.categories TO service_role;
          public          postgres    false    380            t           1259    33915 	   favorites    TABLE     u   CREATE TABLE public.favorites (
    user_id character varying NOT NULL,
    product_id character varying NOT NULL
);
    DROP TABLE public.favorites;
       public         heap    postgres    false    20            �           0    0    TABLE favorites    ACL     �   GRANT ALL ON TABLE public.favorites TO anon;
GRANT ALL ON TABLE public.favorites TO authenticated;
GRANT ALL ON TABLE public.favorites TO service_role;
          public          postgres    false    372            �           1259    50854    order_status    TABLE     Z   CREATE TABLE public.order_status (
    id integer NOT NULL,
    name character varying
);
     DROP TABLE public.order_status;
       public         heap    postgres    false    20            �           0    0    TABLE order_status    ACL     �   GRANT ALL ON TABLE public.order_status TO anon;
GRANT ALL ON TABLE public.order_status TO authenticated;
GRANT ALL ON TABLE public.order_status TO service_role;
          public          postgres    false    388                       1259    50713    orders    TABLE       CREATE TABLE public.orders (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    quantity integer NOT NULL,
    product_example_id integer NOT NULL,
    store_id integer,
    order_date_time bigint,
    status_id integer
);
    DROP TABLE public.orders;
       public         heap    postgres    false    20            �           0    0    TABLE orders    ACL     �   GRANT ALL ON TABLE public.orders TO anon;
GRANT ALL ON TABLE public.orders TO authenticated;
GRANT ALL ON TABLE public.orders TO service_role;
          public          postgres    false    383            u           1259    33927    products    TABLE       CREATE TABLE public.products (
    id character varying NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    price numeric(10,2) NOT NULL,
    image character varying(255),
    seller_id integer,
    category integer DEFAULT 0 NOT NULL
);
    DROP TABLE public.products;
       public         heap    postgres    false    20            �           0    0    TABLE products    ACL     �   GRANT ALL ON TABLE public.products TO anon;
GRANT ALL ON TABLE public.products TO authenticated;
GRANT ALL ON TABLE public.products TO service_role;
          public          postgres    false    373            ~           1259    50695    products_sizes    TABLE       CREATE TABLE public.products_sizes (
    id integer NOT NULL,
    product_id character varying(255) NOT NULL,
    quantity integer NOT NULL,
    size_rus real NOT NULL,
    color character varying(20) NOT NULL,
    image character varying(255),
    stock_id integer NOT NULL
);
 "   DROP TABLE public.products_sizes;
       public         heap    postgres    false    20            �           0    0    TABLE products_sizes    ACL     �   GRANT ALL ON TABLE public.products_sizes TO anon;
GRANT ALL ON TABLE public.products_sizes TO authenticated;
GRANT ALL ON TABLE public.products_sizes TO service_role;
          public          postgres    false    382            �           1259    50873    order_details_view    VIEW       CREATE VIEW public.order_details_view AS
 SELECT o.id AS order_id,
    o.user_id,
    o.quantity AS quantity_in_order,
    o.product_example_id,
    p.id AS product_id,
    p.name,
    o.order_date_time,
    p.price,
    p.image,
    ps.size_rus,
    ps.color,
    os.name AS status
   FROM (((public.orders o
     JOIN public.order_status os ON ((os.id = o.status_id)))
     JOIN public.products_sizes ps ON ((o.product_example_id = ps.id)))
     JOIN public.products p ON (((ps.product_id)::text = (p.id)::text)));
 %   DROP VIEW public.order_details_view;
       public          postgres    false    388    373    373    388    383    373    383    383    382    382    383    373    383    382    383    382    20            �           0    0    TABLE order_details_view    ACL     �   GRANT ALL ON TABLE public.order_details_view TO anon;
GRANT ALL ON TABLE public.order_details_view TO authenticated;
GRANT ALL ON TABLE public.order_details_view TO service_role;
          public          postgres    false    389            �           1259    50853    order_status_id_seq    SEQUENCE     �   CREATE SEQUENCE public.order_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.order_status_id_seq;
       public          postgres    false    388    20            �           0    0    order_status_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.order_status_id_seq OWNED BY public.order_status.id;
          public          postgres    false    387            �           0    0    SEQUENCE order_status_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.order_status_id_seq TO anon;
GRANT ALL ON SEQUENCE public.order_status_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.order_status_id_seq TO service_role;
          public          postgres    false    387            v           1259    33932    products_id_seq    SEQUENCE     �   CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.products_id_seq;
       public          postgres    false    373    20            �           0    0    products_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;
          public          postgres    false    374            �           0    0    SEQUENCE products_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.products_id_seq TO anon;
GRANT ALL ON SEQUENCE public.products_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.products_id_seq TO service_role;
          public          postgres    false    374            }           1259    50694    products_sizes_id_seq    SEQUENCE     �   CREATE SEQUENCE public.products_sizes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.products_sizes_id_seq;
       public          postgres    false    20    382            �           0    0    products_sizes_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.products_sizes_id_seq OWNED BY public.products_sizes.id;
          public          postgres    false    381            �           0    0    SEQUENCE products_sizes_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.products_sizes_id_seq TO anon;
GRANT ALL ON SEQUENCE public.products_sizes_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.products_sizes_id_seq TO service_role;
          public          postgres    false    381            w           1259    33933    sellers    TABLE     �   CREATE TABLE public.sellers (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    image character varying(255)
);
    DROP TABLE public.sellers;
       public         heap    postgres    false    20                        0    0    TABLE sellers    ACL     �   GRANT ALL ON TABLE public.sellers TO anon;
GRANT ALL ON TABLE public.sellers TO authenticated;
GRANT ALL ON TABLE public.sellers TO service_role;
          public          postgres    false    375            x           1259    33938    sellers_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sellers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.sellers_id_seq;
       public          postgres    false    20    375                       0    0    sellers_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.sellers_id_seq OWNED BY public.sellers.id;
          public          postgres    false    376                       0    0    SEQUENCE sellers_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.sellers_id_seq TO anon;
GRANT ALL ON SEQUENCE public.sellers_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.sellers_id_seq TO service_role;
          public          postgres    false    376            �           1259    50790    stocks    TABLE     �   CREATE TABLE public.stocks (
    name character varying,
    address character varying,
    lat real,
    lon real,
    id integer NOT NULL
);
    DROP TABLE public.stocks;
       public         heap    postgres    false    20                       0    0    TABLE stocks    ACL     �   GRANT ALL ON TABLE public.stocks TO anon;
GRANT ALL ON TABLE public.stocks TO authenticated;
GRANT ALL ON TABLE public.stocks TO service_role;
          public          postgres    false    385            �           1259    50797    stocks_id_seq    SEQUENCE     �   CREATE SEQUENCE public.stocks_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.stocks_id_seq;
       public          postgres    false    385    20                       0    0    stocks_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.stocks_id_seq OWNED BY public.stocks.id;
          public          postgres    false    386                       0    0    SEQUENCE stocks_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.stocks_id_seq TO anon;
GRANT ALL ON SEQUENCE public.stocks_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.stocks_id_seq TO service_role;
          public          postgres    false    386            y           1259    33939    stores    TABLE     �   CREATE TABLE public.stores (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    address character varying(255) NOT NULL,
    lat real,
    lon real
);
    DROP TABLE public.stores;
       public         heap    postgres    false    20                       0    0    TABLE stores    ACL     �   GRANT ALL ON TABLE public.stores TO anon;
GRANT ALL ON TABLE public.stores TO authenticated;
GRANT ALL ON TABLE public.stores TO service_role;
          public          postgres    false    377            z           1259    33944    stores_id_seq    SEQUENCE     �   CREATE SEQUENCE public.stores_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.stores_id_seq;
       public          postgres    false    377    20                       0    0    stores_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.stores_id_seq OWNED BY public.stores.id;
          public          postgres    false    378                       0    0    SEQUENCE stores_id_seq    ACL     �   GRANT ALL ON SEQUENCE public.stores_id_seq TO anon;
GRANT ALL ON SEQUENCE public.stores_id_seq TO authenticated;
GRANT ALL ON SEQUENCE public.stores_id_seq TO service_role;
          public          postgres    false    378            {           1259    33945    users    TABLE     �   CREATE TABLE public.users (
    id character varying NOT NULL,
    name character varying(255) NOT NULL,
    image character varying(255),
    phone character varying(36),
    address text
);
    DROP TABLE public.users;
       public         heap    postgres    false    20            	           0    0    TABLE users    ACL     �   GRANT ALL ON TABLE public.users TO anon;
GRANT ALL ON TABLE public.users TO authenticated;
GRANT ALL ON TABLE public.users TO service_role;
          public          postgres    false    379                       2604    50857    order_status id    DEFAULT     r   ALTER TABLE ONLY public.order_status ALTER COLUMN id SET DEFAULT nextval('public.order_status_id_seq'::regclass);
 >   ALTER TABLE public.order_status ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    387    388    388                       2604    34381    products id    DEFAULT     j   ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);
 :   ALTER TABLE public.products ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    374    373                       2604    50698    products_sizes id    DEFAULT     v   ALTER TABLE ONLY public.products_sizes ALTER COLUMN id SET DEFAULT nextval('public.products_sizes_id_seq'::regclass);
 @   ALTER TABLE public.products_sizes ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    382    381    382                       2604    33954 
   sellers id    DEFAULT     h   ALTER TABLE ONLY public.sellers ALTER COLUMN id SET DEFAULT nextval('public.sellers_id_seq'::regclass);
 9   ALTER TABLE public.sellers ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    376    375                       2604    50798 	   stocks id    DEFAULT     f   ALTER TABLE ONLY public.stocks ALTER COLUMN id SET DEFAULT nextval('public.stocks_id_seq'::regclass);
 8   ALTER TABLE public.stocks ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    386    385                       2604    33955 	   stores id    DEFAULT     f   ALTER TABLE ONLY public.stores ALTER COLUMN id SET DEFAULT nextval('public.stores_id_seq'::regclass);
 8   ALTER TABLE public.stores ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    378    377            �          0    50762    buckets 
   TABLE DATA                 public          postgres    false    384   t�       �          0    34326 
   categories 
   TABLE DATA                 public          postgres    false    380   ��       �          0    33915 	   favorites 
   TABLE DATA                 public          postgres    false    372   	�       �          0    50854    order_status 
   TABLE DATA                 public          postgres    false    388   ��       �          0    50713    orders 
   TABLE DATA                 public          postgres    false    383   �       �          0    33927    products 
   TABLE DATA                 public          postgres    false    373   ��       �          0    50695    products_sizes 
   TABLE DATA                 public          postgres    false    382   Ո       �          0    33933    sellers 
   TABLE DATA                 public          postgres    false    375   \�       �          0    50790    stocks 
   TABLE DATA                 public          postgres    false    385   ��       �          0    33939    stores 
   TABLE DATA                 public          postgres    false    377   h�       �          0    33945    users 
   TABLE DATA                 public          postgres    false    379   8�       
           0    0    order_status_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.order_status_id_seq', 1, false);
          public          postgres    false    387                       0    0    products_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.products_id_seq', 2, true);
          public          postgres    false    374                       0    0    products_sizes_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.products_sizes_id_seq', 1, true);
          public          postgres    false    381                       0    0    sellers_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.sellers_id_seq', 2, true);
          public          postgres    false    376                       0    0    stocks_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.stocks_id_seq', 1, false);
          public          postgres    false    386                       0    0    stores_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.stores_id_seq', 2, true);
          public          postgres    false    378            ,           2606    50768    buckets buckets_pk 
   CONSTRAINT     i   ALTER TABLE ONLY public.buckets
    ADD CONSTRAINT buckets_pk PRIMARY KEY (user_id, product_example_id);
 <   ALTER TABLE ONLY public.buckets DROP CONSTRAINT buckets_pk;
       public            postgres    false    384    384            $           2606    34426    categories category_pk 
   CONSTRAINT     T   ALTER TABLE ONLY public.categories
    ADD CONSTRAINT category_pk PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.categories DROP CONSTRAINT category_pk;
       public            postgres    false    380                       2606    42453    favorites favorites_pk 
   CONSTRAINT     e   ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_pk PRIMARY KEY (user_id, product_id);
 @   ALTER TABLE ONLY public.favorites DROP CONSTRAINT favorites_pk;
       public            postgres    false    372    372            0           2606    50861    order_status order_status_pk 
   CONSTRAINT     Z   ALTER TABLE ONLY public.order_status
    ADD CONSTRAINT order_status_pk PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.order_status DROP CONSTRAINT order_status_pk;
       public            postgres    false    388            (           2606    50719    orders orders_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public            postgres    false    383            *           2606    50789    orders orders_unique 
   CONSTRAINT     a   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_unique UNIQUE (id, product_example_id);
 >   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_unique;
       public            postgres    false    383    383                       2606    34383    products products_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.products DROP CONSTRAINT products_pkey;
       public            postgres    false    373            &           2606    50702 "   products_sizes products_sizes_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.products_sizes
    ADD CONSTRAINT products_sizes_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.products_sizes DROP CONSTRAINT products_sizes_pkey;
       public            postgres    false    382                       2606    33965    sellers sellers_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.sellers
    ADD CONSTRAINT sellers_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.sellers DROP CONSTRAINT sellers_pkey;
       public            postgres    false    375            .           2606    50805    stocks stocks_pk 
   CONSTRAINT     N   ALTER TABLE ONLY public.stocks
    ADD CONSTRAINT stocks_pk PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.stocks DROP CONSTRAINT stocks_pk;
       public            postgres    false    385                        2606    33967    stores stores_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.stores
    ADD CONSTRAINT stores_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.stores DROP CONSTRAINT stores_pkey;
       public            postgres    false    377            "           2606    33969    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    379            ;           2606    50769 !   buckets buckets_products_sizes_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.buckets
    ADD CONSTRAINT buckets_products_sizes_fk FOREIGN KEY (product_example_id) REFERENCES public.products_sizes(id) ON UPDATE CASCADE ON DELETE CASCADE;
 K   ALTER TABLE ONLY public.buckets DROP CONSTRAINT buckets_products_sizes_fk;
       public          postgres    false    3878    384    382            <           2606    50774    buckets cart_user_id_fkey    FK CONSTRAINT     x   ALTER TABLE ONLY public.buckets
    ADD CONSTRAINT cart_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);
 C   ALTER TABLE ONLY public.buckets DROP CONSTRAINT cart_user_id_fkey;
       public          postgres    false    384    379    3874            1           2606    34415    favorites favorites_products_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_products_fk FOREIGN KEY (product_id) REFERENCES public.products(id);
 I   ALTER TABLE ONLY public.favorites DROP CONSTRAINT favorites_products_fk;
       public          postgres    false    373    3868    372            2           2606    33985     favorites favorites_user_id_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.favorites
    ADD CONSTRAINT favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);
 J   ALTER TABLE ONLY public.favorites DROP CONSTRAINT favorites_user_id_fkey;
       public          postgres    false    3874    372    379            7           2606    50862    orders orders_order_status_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_order_status_fk FOREIGN KEY (status_id) REFERENCES public.order_status(id);
 G   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_order_status_fk;
       public          postgres    false    388    3888    383            8           2606    50725 %   orders orders_product_example_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_product_example_id_fkey FOREIGN KEY (product_example_id) REFERENCES public.products_sizes(id);
 O   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_product_example_id_fkey;
       public          postgres    false    383    3878    382            9           2606    50831    orders orders_stores_fk    FK CONSTRAINT     x   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_stores_fk FOREIGN KEY (store_id) REFERENCES public.stores(id);
 A   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_stores_fk;
       public          postgres    false    3872    377    383            :           2606    50720    orders orders_user_id_fkey    FK CONSTRAINT     y   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);
 D   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_user_id_fkey;
       public          postgres    false    3874    383    379            3           2606    50781    products products_categories_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_categories_fk FOREIGN KEY (category) REFERENCES public.categories(id);
 I   ALTER TABLE ONLY public.products DROP CONSTRAINT products_categories_fk;
       public          postgres    false    3876    380    373            4           2606    34000     products products_seller_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_seller_id_fkey FOREIGN KEY (seller_id) REFERENCES public.sellers(id);
 J   ALTER TABLE ONLY public.products DROP CONSTRAINT products_seller_id_fkey;
       public          postgres    false    3870    375    373            5           2606    50703 -   products_sizes products_sizes_product_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.products_sizes
    ADD CONSTRAINT products_sizes_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);
 W   ALTER TABLE ONLY public.products_sizes DROP CONSTRAINT products_sizes_product_id_fkey;
       public          postgres    false    373    3868    382            6           2606    50826 '   products_sizes products_sizes_stocks_fk    FK CONSTRAINT     �   ALTER TABLE ONLY public.products_sizes
    ADD CONSTRAINT products_sizes_stocks_fk FOREIGN KEY (stock_id) REFERENCES public.stocks(id);
 Q   ALTER TABLE ONLY public.products_sizes DROP CONSTRAINT products_sizes_stocks_fk;
       public          postgres    false    3886    385    382            �           3256    34158 &   users Enable read access for all users    POLICY     O   CREATE POLICY "Enable read access for all users" ON public.users USING (true);
 @   DROP POLICY "Enable read access for all users" ON public.users;
       public          postgres    false    379            �
           826    16453     DEFAULT PRIVILEGES FOR SEQUENCES    DEFAULT ACL     �  ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES  TO service_role;
          public          postgres    false    20            �
           826    16454     DEFAULT PRIVILEGES FOR SEQUENCES    DEFAULT ACL     �  ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON SEQUENCES  TO service_role;
          public          supabase_admin    false    20            �
           826    16452     DEFAULT PRIVILEGES FOR FUNCTIONS    DEFAULT ACL     �  ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON FUNCTIONS  TO service_role;
          public          postgres    false    20            �
           826    16456     DEFAULT PRIVILEGES FOR FUNCTIONS    DEFAULT ACL     �  ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON FUNCTIONS  TO service_role;
          public          supabase_admin    false    20            �
           826    16451    DEFAULT PRIVILEGES FOR TABLES    DEFAULT ACL     }  ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES  TO service_role;
          public          postgres    false    20            �
           826    16455    DEFAULT PRIVILEGES FOR TABLES    DEFAULT ACL     �  ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES  TO postgres;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES  TO anon;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES  TO authenticated;
ALTER DEFAULT PRIVILEGES FOR ROLE supabase_admin IN SCHEMA public GRANT ALL ON TABLES  TO service_role;
          public          supabase_admin    false    20            �   
   x���          �   k   x���v
Q���W((M��L�KN,IM�/�L-Vs�	uV�0�QPI���,V״��$J�P�{f.������2�S��c�SZ���_D�&S���R�o��A��� <4O�      �   g   x���v
Q���W((M��L�KK,�/�,I-Vs�	uV�P7771JIL1�M����5�HN�MJ�4�54�HL13KI563U�QP7V״�򤪑�40� d& ��E�      �   v   x���v
Q���W((M��L��/JI-�/.I,)-Vs�	uV�0�QP�0���x�}[����/l��W]Ӛ˓H��Ќ�4��b���tÌ��a߅��/���� ��f+      �   �   x��=�0��=��(�i>C��T�ս�% J��oqy��w��:�0�x����*e���t��촐5�c庠�ȁ<�H�%�K��D��E��#:�S���EB�b�ߜi��k��&}�t��R���#}      �   (  x����S�@���}s�J(�'uu�T�@ݽI�L1LS30���L�� �BHҿz��K0��B�:�E>Q2n.%y�,�_?�ݍ��itz�9C�����/Z���J��N �yJƉ�B�����t��V.��f����&��Z�V��5z�'�8���T���s�q��qC*�����`g�NU�:F{4)��p��HD+&�DN��i���#�	��^�pF&wʹ�N3��$��[�������$�QK����]qu62��:�����-�A�x������ł-��|q�O��\�(�J�B�Πs(ʥ��@3C��s�d�HC���J5*10���:�zN;'bw�,�㫷��a��^��{21B8׏ٚx'����b��y��j~�����D&����7���~}Nw�p���
,Z������\p̃ݯb�(��-i^�&ey<�WQ(��Z);a�JX�B/��#b�pRp���9Ұ"��[���w�uD����=���)p���j�tX'��+����n]�Wt�qu���L���v����(�yI��1��,��K+7!P��L�=�\p�s|x��PƬ^C�De��9�vv�|��%�RIW��N��G0]��o:AΑ�Sn�a�>��LhGsp}�i)B'�QKl�T*�V�{�8���1D3n�̛W&읣���`�@��in[��R�2��\��P!6WW�e��q{����P�C隧d�MQ���M���;X��{�ޅ!�k�3"U�3��{��Ҕ���T�ٳu����?S٩      �   w   x���v
Q���W((M��L�+(�O)M.)�/άJ-Vs�	uV�0�QP7T�Q04�Q0q*��L,��\́��Fzy� iMk.O�5�
�M��d�1̡Hf�Y�����4F2�� ��D[      �   t   x���v
Q���W((M��L�+N��I-*Vs�	uV�0�QPRJ-N�IU�Q���Ѵ��$����71%U�3O�9#3/�[]�8�F@��E��@]`[��g T�Z\�p( �+B�      �   x   x���v
Q���W((M��L�+.�O�.Vs�	uV�P�����{/6]l�����=@v����:
��\�w��®�.l�;.�T05J����(������(jZsqq ��0      �   �   x���;
�PE��b�(�G~/V��o�� (F{�AA��J Q�l�fG΋��˙a��aw0"?�i��.3nW�yH�No�R�2H�o�x �N�tE�H�#er��gF(�� �浴��J�e�c�R����l�����+
|ꮨ�#果IB�)u�x"�Y�J�>:
r�R1��jב�h��.l�      �   b   x���v
Q���W((M��L�+-N-*Vs�	uV�P7771JIL1�M����5�HN�MJ�4�54�HL13KI563U�QPwM�/�~�>>Ȥ�5 �     