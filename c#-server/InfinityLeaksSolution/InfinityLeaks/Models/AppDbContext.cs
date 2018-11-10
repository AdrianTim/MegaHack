using InfinityLeaks.Models.Data;
using System.Data.Entity;

namespace InfinityLeaks.Models
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(string connectionString)
        {
            Database.Connection.ConnectionString = connectionString;
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            //modelBuilder.Configurations.Add(new StudentConfigurations());

            //modelBuilder.Entity<Teacher>()
            //    .ToTable("TeacherInfo");

            //modelBuilder.Entity<Teacher>()
            //    .MapToStoredProcedures();
        }

        public DbSet<Phone> Phones { get; set; }
    }
}